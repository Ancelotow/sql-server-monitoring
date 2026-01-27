package org.ancelotow.sqlservermonitoring.ui.tabs.monitoring_tab

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intellij.openapi.project.Project
import kotlinx.coroutines.launch
import org.ancelotow.sqlservermonitoring.domain.uses_cases.GetDataSourcesState
import org.ancelotow.sqlservermonitoring.domain.uses_cases.GetDataSourcesUseCase
import org.ancelotow.sqlservermonitoring.ui.models.DefaultViewModel
import org.ancelotow.sqlservermonitoring.ui.models.ErrorType

class MonitoringTabViewModel(
    private val getDataSourcesUseCase: GetDataSourcesUseCase
) :  ViewModel(), DefaultViewModel<MonitoringTabState, MonitoringTabEvent> {

    override var state by mutableStateOf(MonitoringTabState().init())
        private set

    override fun onEvent(event: MonitoringTabEvent) {
        when(event){
            is MonitoringTabEvent.FetchSources -> fetchSources(event.project)
        }
    }

    private fun fetchSources(project: Project){
        viewModelScope.launch {
            state = state.init()
            getDataSourcesUseCase(project).collect {
                state = when(it){
                    is GetDataSourcesState.Loading -> {
                        state.loading()
                    }

                    is GetDataSourcesState.Success -> {
                        state.success(it.dataSources)
                    }

                    is GetDataSourcesState.Error -> {
                        state.error(errors = listOf(ErrorType.UNKNOWN_ERROR))
                    }
                }

            }
        }
    }
}