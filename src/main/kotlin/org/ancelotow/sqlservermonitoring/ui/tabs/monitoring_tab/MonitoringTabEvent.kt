package org.ancelotow.sqlservermonitoring.ui.tabs.monitoring_tab

import com.intellij.database.dataSource.LocalDataSource
import com.intellij.openapi.project.Project
import org.ancelotow.sqlservermonitoring.ui.models.ViewModelEvent

sealed class MonitoringTabEvent : ViewModelEvent {

    data class FetchSources(val project: Project) : MonitoringTabEvent()
    data class SelectSource(val dataSource: LocalDataSource) : MonitoringTabEvent()
    data class FetchMonitor(val dataSource: LocalDataSource) : MonitoringTabEvent()

}