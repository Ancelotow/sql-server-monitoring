package org.ancelotow.sqlservermonitoring.ui.tabs.monitoring_tab

import com.intellij.openapi.project.Project
import org.ancelotow.sqlservermonitoring.ui.models.ViewModelEvent

sealed class MonitoringTabEvent : ViewModelEvent {

    data class FetchSources(val project: Project) : MonitoringTabEvent()

}