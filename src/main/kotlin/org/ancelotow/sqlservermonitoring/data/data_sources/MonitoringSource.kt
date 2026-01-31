package org.ancelotow.sqlservermonitoring.data.data_sources

import com.intellij.database.dataSource.LocalDataSource
import com.intellij.openapi.project.Project
import org.ancelotow.sqlservermonitoring.data.data_sources.dto.MonitorDto

interface MonitoringSource {

    suspend fun getMonitor(project: Project, dataSource: LocalDataSource): MonitorDto

}