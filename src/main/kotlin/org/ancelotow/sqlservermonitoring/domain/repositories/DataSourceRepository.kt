package org.ancelotow.sqlservermonitoring.domain.repositories

import com.intellij.database.dataSource.LocalDataSource
import com.intellij.openapi.project.Project

interface DataSourceRepository {

    suspend fun getDataSources(project: Project): List<LocalDataSource>

}