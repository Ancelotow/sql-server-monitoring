package org.ancelotow.sqlservermonitoring.data.data_sources.local

import com.intellij.database.console.session.DatabaseSessionManager.getSession
import com.intellij.database.dataSource.DatabaseConnectionCore
import com.intellij.database.dataSource.LocalDataSource
import com.intellij.database.datagrid.DataRequest.RawRequest
import com.intellij.openapi.project.Project
import kotlinx.coroutines.suspendCancellableCoroutine
import org.ancelotow.sqlservermonitoring.data.data_sources.MonitoringSource
import org.ancelotow.sqlservermonitoring.data.data_sources.dto.MonitorDto
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class MonitoringLocalSource : MonitoringSource {

    private var prevDatabaseIO: Long = 0
    private var prevBatchRequests: Long = 0
    private var prevTimestamp: Long = System.currentTimeMillis()

    override suspend fun getMonitor(project: Project, dataSource: LocalDataSource): MonitorDto {
        val querySql = """
            ;WITH rb AS
            (
                SELECT TOP (1)
                    rb.[timestamp],
                    CONVERT(xml, rb.record) AS record_xml
                FROM sys.dm_os_ring_buffers AS rb
                WHERE rb.ring_buffer_type = N'RING_BUFFER_SCHEDULER_MONITOR'
                    AND rb.record LIKE '%<SystemHealth>%'
                ORDER BY rb.[timestamp] DESC
            )
            SELECT
             SQLProcessUtilization = rb.record_xml.value('(//SystemHealth/ProcessUtilization)[1]', 'int'),
             WaitingTasks = (SELECT COUNT(*)
                             FROM sys.dm_exec_requests
                             WHERE status = 'suspended'),
             DatabaseIO_MBps = (
                 SELECT SUM(num_of_bytes_read + num_of_bytes_written) * 1.0 / 1024 / 1024
                 FROM sys.dm_io_virtual_file_stats(NULL, NULL)
             ),
             BatchRequestsPerSec = (
                 SELECT cntr_value
                 FROM sys.dm_os_performance_counters
                 WHERE counter_name = 'Batch Requests/sec'
                     AND instance_name = ''
             ),
             TempDB_MB = (
                 SELECT FILEPROPERTY(name, 'SpaceUsed')*8.0/1024 AS Used_MB
                 FROM sys.database_files
                 WHERE type = 1
             ),
             RAM_Usage = (
                 SELECT  (total_physical_memory_kb - available_physical_memory_kb) / 1024.0
                 FROM sys.dm_os_sys_memory
                 )
            FROM rb;
        """.trimIndent()

        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
        val session = getSession(project, dataSource)
        return suspendCancellableCoroutine { cont ->
            val req: RawRequest = object : RawRequest(session) {
                override fun processRaw(context: Context?, databaseConnectionCore: DatabaseConnectionCore) {
                    try {
                        val conn = databaseConnectionCore.remoteConnection
                        val stmt = conn.prepareStatement(querySql)
                        stmt.queryTimeout = 5
                        stmt.execute()
                        val rs = stmt.resultSet
                        if (rs.next()) {
                            val proc = rs.getInt("SQLProcessUtilization")
                            val waitingTasks = rs.getInt("WaitingTasks")
                            val databaseIONow = rs.getLong("DatabaseIO_MBps")
                            val batchRequestNow = rs.getLong("BatchRequestsPerSec")
                            val tempDbLog = rs.getLong("TempDB_MB")
                            val ramUsageNow = rs.getLong("RAM_Usage")
                            val now = System.currentTimeMillis()
                            val intervalSec = (now - prevTimestamp) / 1000.0

                            val databaseIO = if (prevDatabaseIO != 0L) (databaseIONow - prevDatabaseIO) / intervalSec else 0.0
                            val batchRequest = if (prevBatchRequests != 0L) (batchRequestNow - prevBatchRequests) / intervalSec else 0.0

                            prevDatabaseIO = databaseIONow
                            prevBatchRequests = batchRequestNow
                            prevTimestamp = now

                            val monitor = MonitorDto(
                                processorTime = proc,
                                waitingTask = waitingTasks,
                                databaseIO = databaseIO.toInt(),
                                batchRequests = batchRequest.toInt(),
                                ramUsage = ramUsageNow.toInt(),
                                tempDbDiskUsage = tempDbLog.toInt()
                            )
                            cont.resume(monitor)
                        }
                    } catch (e: Exception) {
                        cont.resumeWithException(e)
                    }
                }
            }
            session.messageBus.dataProducer.processRequest(req)
        }
    }


}