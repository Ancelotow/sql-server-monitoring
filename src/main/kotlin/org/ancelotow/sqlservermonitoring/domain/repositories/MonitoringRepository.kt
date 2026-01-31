package org.ancelotow.sqlservermonitoring.domain.repositories

import com.intellij.database.dataSource.LocalDataSource
import org.ancelotow.sqlservermonitoring.domain.entities.Monitor

interface MonitoringRepository {

    suspend fun getMonitor(dataSource: LocalDataSource): Monitor

}