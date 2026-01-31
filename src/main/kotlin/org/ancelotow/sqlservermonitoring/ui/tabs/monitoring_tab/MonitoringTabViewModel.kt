package org.ancelotow.sqlservermonitoring.ui.tabs.monitoring_tab

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.intellij.database.dataSource.LocalDataSource
import com.intellij.openapi.project.Project
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.ancelotow.sqlservermonitoring.domain.interactors.MonitoringInteractor
import org.ancelotow.sqlservermonitoring.domain.uses_cases.GetDataSourcesState
import org.ancelotow.sqlservermonitoring.domain.uses_cases.GetMonitorState
import org.ancelotow.sqlservermonitoring.domain.uses_cases.GetMonitorUseCase
import org.ancelotow.sqlservermonitoring.ui.models.DefaultViewModel
import org.ancelotow.sqlservermonitoring.ui.models.ErrorType

class MonitoringTabViewModel(
    private val interactor: MonitoringInteractor
) :  ViewModel(), DefaultViewModel<MonitoringTabState, MonitoringTabEvent> {

    override var state by mutableStateOf(MonitoringTabState().init())
        private set

    private var isMonitoringActive = false

    var selectedDataSource by mutableStateOf<LocalDataSource?>(null)
        private set

    override fun onEvent(event: MonitoringTabEvent) {
        when(event){
            is MonitoringTabEvent.FetchSources -> fetchSources(event.project)
            is MonitoringTabEvent.SelectSource -> selectDataSource(event.dataSource)
            is MonitoringTabEvent.FetchMonitor -> fetchMonitor(event.dataSource)
        }
    }

    private fun fetchSources(project: Project){
        viewModelScope.launch {
            state = state.init()
            interactor.getDataSources(project).collect {
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

    private fun selectDataSource(dataSource: LocalDataSource){
        startMonitoring(dataSource)
        selectedDataSource = dataSource
    }

    private fun fetchMonitor(dataSource: LocalDataSource){
        viewModelScope.launch {
            state = state.init()
            interactor.getMonitor(dataSource).collect {
                state = when(it){
                    is GetMonitorState.Loading -> {
                        state.loading()
                    }

                    is GetMonitorState.Success -> {
                        state.success(state.sources, it.monitor)
                    }

                    is GetMonitorState.Error -> {
                        state.error(errors = listOf(ErrorType.UNKNOWN_ERROR))
                    }
                }

            }
        }
    }

    private fun startMonitoring(dataSource: LocalDataSource) {
        isMonitoringActive = true
        viewModelScope.launch {
            while (isMonitoringActive) {
                fetchMonitor(dataSource)
                delay(MONITORING_INTERVAL_MS)
            }
        }
    }

    fun stopMonitoring() {
        isMonitoringActive = false
    }

    companion object {
        const val MONITORING_INTERVAL_MS = 1000L
        const val CAPACITY = 120
    }

}