package org.ancelotow.sqlservermonitoring.data.data_sources.local

import com.intellij.database.dataSource.LocalDataSource
import org.ancelotow.sqlservermonitoring.data.data_sources.MonitoringSource
import org.ancelotow.sqlservermonitoring.data.data_sources.dto.MonitorDto
import java.sql.Connection
import java.sql.SQLException

class MonitoringLocalSource : MonitoringSource {

    override suspend fun getMonitor(dataSource: LocalDataSource): MonitorDto {
        val sql = """
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

        val connection: Connection? = try {
            val method = dataSource.javaClass.getMethod("getConnection")
            method.invoke(dataSource) as? Connection
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

        connection?.use { conn ->
            try {
                conn.prepareStatement(sql).use { stmt ->
                    stmt.executeQuery().use { rs ->
                        if (rs.next()) {
                            val proc = rs.getInt("SQLProcessUtilization")
                            val idle = rs.getInt("SystemIdle")
                            val other = rs.getInt("OtherProcessUtilization")
                            val sample = rs.getInt("SampleTime")
                            return MonitorDto(proc, idle, other, sample)
                        }
                    }
                }
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }

        return MonitorDto(0, 0, 0, 0)
    }


}