package org.ancelotow.sqlservermonitoring.data.data_sources.local

import com.intellij.database.Dbms
import com.intellij.database.dataSource.LocalDataSource
import com.intellij.database.dataSource.LocalDataSourceManager
import com.intellij.openapi.project.Project
import org.ancelotow.sqlservermonitoring.data.data_sources.DataSourceSource

class DataSourceLocalSource : DataSourceSource {

    override suspend fun getDataSources(project: Project): List<LocalDataSource> {
        val manager = LocalDataSourceManager.getInstance(project)
        return manager.dataSources.filter { ds -> ds.dbms == Dbms.MSSQL }
    }

}