package org.ancelotow.sqlservermonitoring.data.data_sources

import com.intellij.database.dataSource.LocalDataSource
import com.intellij.openapi.project.Project

interface DataSourceSource {

    suspend fun getDataSources(project: Project): List<LocalDataSource>

}