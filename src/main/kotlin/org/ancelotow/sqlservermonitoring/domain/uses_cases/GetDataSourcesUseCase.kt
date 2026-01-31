package org.ancelotow.sqlservermonitoring.domain.uses_cases

import com.intellij.database.dataSource.LocalDataSource
import com.intellij.openapi.project.Project
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.ancelotow.sqlservermonitoring.domain.repositories.DataSourceRepository

class GetDataSourcesUseCase(
    private val repository: DataSourceRepository
) {

    operator fun invoke(project: Project): Flow<GetDataSourcesState> {
        return flow {
            emit(GetDataSourcesState.Loading)
            try {
                val dataSources = repository.getDataSources(project)
                emit(GetDataSourcesState.Success(dataSources))
            } catch (ex: Exception) {
                emit(GetDataSourcesState.Error("Failed to fetch data sources: ${ex.message}"))
            }
        }
    }

}

sealed class GetDataSourcesState {
    data object Loading: GetDataSourcesState()
    data class Success(val dataSources: List<LocalDataSource>): GetDataSourcesState()
    data class Error(val message: String): GetDataSourcesState()
}