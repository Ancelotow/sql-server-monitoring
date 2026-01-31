package org.ancelotow.sqlservermonitoring.ui.tabs.monitoring_tab

import com.intellij.database.dataSource.LocalDataSource
import org.ancelotow.sqlservermonitoring.domain.entities.Monitor
import org.ancelotow.sqlservermonitoring.ui.models.DefaultState
import org.ancelotow.sqlservermonitoring.ui.models.DefaultStateStatus
import org.ancelotow.sqlservermonitoring.ui.models.ErrorType
import org.ancelotow.sqlservermonitoring.ui.models.ViewModelState

class MonitoringTabState(
    val sources: List<LocalDataSource> = emptyList(),
    val monitor: Monitor = Monitor.empty,
    status: DefaultStateStatus = DefaultStateStatus.INITIAL,
    errors: List<ErrorType> = emptyList()
) :  DefaultState<MonitoringTabState>(status, errors), ViewModelState {

    override fun init(): MonitoringTabState {
        return MonitoringTabState()
    }

    override fun loading(): MonitoringTabState {
        return MonitoringTabState(status = DefaultStateStatus.LOADING)
    }

    fun success(sources: List<LocalDataSource>, monitor: Monitor = Monitor.empty): MonitoringTabState {
        return MonitoringTabState(status = DefaultStateStatus.SUCCESS, sources = sources, monitor = monitor)
    }

    override fun error(errors: List<ErrorType>): MonitoringTabState {
        return MonitoringTabState(status = DefaultStateStatus.ERROR, errors = errors)
    }

}