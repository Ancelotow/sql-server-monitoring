package org.ancelotow.sqlservermonitoring.data.data_sources

import com.intellij.database.dataSource.LocalDataSource
import org.ancelotow.sqlservermonitoring.data.data_sources.dto.MonitorDto

interface MonitoringSource {

    suspend fun getMonitor(dataSource: LocalDataSource): MonitorDto

}