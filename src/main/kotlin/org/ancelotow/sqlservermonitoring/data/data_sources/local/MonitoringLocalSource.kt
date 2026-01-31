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
                SystemIdle = rb.record_xml.value('(//SystemHealth/SystemIdle)[1]', 'int'), 
                OtherProcessUtilization = 
                    100 
                    - rb.record_xml.value('(//SystemHealth/ProcessUtilization)[1]', 'int') 
                    - rb.record_xml.value('(//SystemHealth/SystemIdle)[1]', 'int'), 
                SampleTime = rb.[timestamp] 
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
                        val resultSet = stmt.resultSet
                        if (resultSet.next()) {
                            val proc = resultSet.getInt("SQLProcessUtilization")
                            val idle = resultSet.getInt("SystemIdle")
                            val other = resultSet.getInt("OtherProcessUtilization")
                            val sample = resultSet.getInt("SampleTime")
                            cont.resume(MonitorDto(proc, idle, other, sample))
                        } else {
                            cont.resumeWithException(Exception("No data returned from query"))
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