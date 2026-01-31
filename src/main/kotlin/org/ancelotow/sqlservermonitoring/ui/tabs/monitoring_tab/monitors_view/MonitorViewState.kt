package org.ancelotow.sqlservermonitoring.ui.tabs.monitoring_tab.monitors_view

import org.ancelotow.sqlservermonitoring.domain.entities.Monitor
import org.ancelotow.sqlservermonitoring.ui.models.DefaultState
import org.ancelotow.sqlservermonitoring.ui.models.DefaultStateStatus
import org.ancelotow.sqlservermonitoring.ui.models.ErrorType
import org.ancelotow.sqlservermonitoring.ui.models.ViewModelState

class MonitorViewState(
    val monitor: Monitor = Monitor.empty,
    status: DefaultStateStatus = DefaultStateStatus.INITIAL,
    errors: List<ErrorType> = emptyList()
) :  DefaultState<MonitorViewState>(status, errors), ViewModelState {

    override fun init(): MonitorViewState {
        return MonitorViewState()
    }

    override fun loading(): MonitorViewState {
        return MonitorViewState(status = DefaultStateStatus.LOADING)
    }

    fun success(monitor: Monitor): MonitorViewState {
        return MonitorViewState(status = DefaultStateStatus.SUCCESS, monitor = monitor)
    }

    override fun error(errors: List<ErrorType>): MonitorViewState {
        return MonitorViewState(status = DefaultStateStatus.ERROR, errors = errors)
    }

}