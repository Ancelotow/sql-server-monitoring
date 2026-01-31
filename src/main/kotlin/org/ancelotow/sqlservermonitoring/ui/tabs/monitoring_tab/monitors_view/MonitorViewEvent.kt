package org.ancelotow.sqlservermonitoring.ui.tabs.monitoring_tab.monitors_view

import com.intellij.database.dataSource.LocalDataSource
import com.intellij.openapi.project.Project
import org.ancelotow.sqlservermonitoring.ui.models.ViewModelEvent

sealed class MonitorViewEvent : ViewModelEvent {

    data class StartFetching(val project: Project, val dataSource: LocalDataSource) : MonitorViewEvent()
    data object StopFetching : MonitorViewEvent()
    data class ResumeFetching(val project: Project, val dataSource: LocalDataSource) : MonitorViewEvent()

}