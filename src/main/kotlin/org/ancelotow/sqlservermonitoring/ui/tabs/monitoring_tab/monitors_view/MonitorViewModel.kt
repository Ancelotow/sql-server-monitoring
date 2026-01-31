package org.ancelotow.sqlservermonitoring.ui.tabs.monitoring_tab.monitors_view

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
import org.ancelotow.sqlservermonitoring.domain.uses_cases.GetMonitorState
import org.ancelotow.sqlservermonitoring.ui.models.DefaultViewModel
import org.ancelotow.sqlservermonitoring.ui.models.ErrorType

class MonitorViewModel(
    private val interactor: MonitoringInteractor
) :  ViewModel(), DefaultViewModel<MonitorViewState, MonitorViewEvent> {
    override var state by mutableStateOf(MonitorViewState().init())
        private set

    private var isMonitoringActive = false

    override fun onEvent(event: MonitorViewEvent) {
        when(event){
            is MonitorViewEvent.FetchMonitor -> fetchMonitor(event.project, event.dataSource)
            is MonitorViewEvent.StartFetching -> startMonitoring(event.project, event.dataSource)
        }
    }

    private fun fetchMonitor(project: Project, dataSource: LocalDataSource){
        viewModelScope.launch {
            state = state.init()
            interactor.getMonitor(project, dataSource).collect {
                state = when(it){
                    is GetMonitorState.Loading -> {
                        state.loading()
                    }

                    is GetMonitorState.Success -> {
                        state.success(it.monitor)
                    }

                    is GetMonitorState.Error -> {
                        state.error(errors = listOf(ErrorType.UNKNOWN_ERROR))
                    }
                }

            }
        }
    }

    private fun startMonitoring(project: Project, dataSource: LocalDataSource) {
        isMonitoringActive = true
        viewModelScope.launch {
            while (isMonitoringActive) {
                fetchMonitor(project, dataSource)
                delay(MONITORING_INTERVAL_MS)
            }
        }
    }

    companion object {
        const val MONITORING_INTERVAL_MS = 1000L
        const val CAPACITY = 120
    }
}