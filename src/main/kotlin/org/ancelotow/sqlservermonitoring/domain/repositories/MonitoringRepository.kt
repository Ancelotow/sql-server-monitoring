package org.ancelotow.sqlservermonitoring.domain.repositories

import com.intellij.database.dataSource.LocalDataSource
import com.intellij.openapi.project.Project
import org.ancelotow.sqlservermonitoring.domain.entities.Monitor

interface MonitoringRepository {

    suspend fun getMonitor(project: Project, dataSource: LocalDataSource): Monitor

}