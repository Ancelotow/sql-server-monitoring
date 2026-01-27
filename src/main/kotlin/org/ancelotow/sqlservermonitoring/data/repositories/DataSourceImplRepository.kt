package org.ancelotow.sqlservermonitoring.data.repositories

import com.intellij.database.dataSource.LocalDataSource
import com.intellij.openapi.project.Project
import org.ancelotow.sqlservermonitoring.data.data_sources.DataSourceSource
import org.ancelotow.sqlservermonitoring.domain.repositories.DataSourceRepository

class DataSourceImplRepository(
    private val dataSource: DataSourceSource
) : DataSourceRepository {

    override suspend fun getDataSources(project: Project): List<LocalDataSource> {
        return dataSource.getDataSources(project)
    }

}