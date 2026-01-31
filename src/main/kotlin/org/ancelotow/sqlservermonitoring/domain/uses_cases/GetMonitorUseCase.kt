package org.ancelotow.sqlservermonitoring.domain.uses_cases

import com.intellij.database.dataSource.LocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.ancelotow.sqlservermonitoring.domain.entities.Monitor
import org.ancelotow.sqlservermonitoring.domain.repositories.MonitoringRepository

class GetMonitorUseCase(
    private val repository: MonitoringRepository
) {
    operator fun invoke(dataSource: LocalDataSource): Flow<GetMonitorState> {
        return flow {
            emit(GetMonitorState.Loading)
            try {
                val monitor = repository.getMonitor(dataSource)
                emit(GetMonitorState.Success(monitor))
            } catch (ex: Exception) {
                emit(GetMonitorState.Error("Failed to fetch monitor: ${ex.message}"))
            }
        }
    }
}


sealed class GetMonitorState {
    data object Loading: GetMonitorState()
    data class Success(val monitor: Monitor): GetMonitorState()
    data class Error(val message: String): GetMonitorState()
}