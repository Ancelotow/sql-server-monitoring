package org.ancelotow.sqlservermonitoring.data.repositories

import com.intellij.database.dataSource.LocalDataSource
import org.ancelotow.sqlservermonitoring.data.data_sources.MonitoringSource
import org.ancelotow.sqlservermonitoring.data.mappers.MonitorMapper
import org.ancelotow.sqlservermonitoring.domain.entities.Monitor
import org.ancelotow.sqlservermonitoring.domain.repositories.MonitoringRepository

class MonitoringImplRepository(
    private val source: MonitoringSource,
    private val mapper: MonitorMapper
) : MonitoringRepository {

    override suspend fun getMonitor(dataSource: LocalDataSource): Monitor {
        val dto = source.getMonitor(dataSource)
        return mapper.rehydrate(dto)
    }

}