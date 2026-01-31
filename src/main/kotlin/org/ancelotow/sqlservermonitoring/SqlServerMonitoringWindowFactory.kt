package org.ancelotow.sqlservermonitoring

import androidx.compose.runtime.LaunchedEffect
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import org.ancelotow.sqlservermonitoring.data.data_sources.local.DataSourceLocalSource
import org.ancelotow.sqlservermonitoring.data.data_sources.local.MonitoringLocalSource
import org.ancelotow.sqlservermonitoring.data.mappers.MonitorMapper
import org.ancelotow.sqlservermonitoring.data.repositories.DataSourceImplRepository
import org.ancelotow.sqlservermonitoring.data.repositories.MonitoringImplRepository
import org.ancelotow.sqlservermonitoring.domain.interactors.MonitoringInteractor
import org.ancelotow.sqlservermonitoring.domain.uses_cases.GetDataSourcesUseCase
import org.ancelotow.sqlservermonitoring.domain.uses_cases.GetMonitorUseCase
import org.ancelotow.sqlservermonitoring.ui.tabs.monitoring_tab.MonitoringTab
import org.ancelotow.sqlservermonitoring.ui.tabs.monitoring_tab.MonitoringTabViewModel
import org.ancelotow.sqlservermonitoring.ui.tabs.monitoring_tab.monitors_view.MonitorViewModel
import org.ancelotow.sqlservermonitoring.ui.theme.MyMessageBundle
import org.ancelotow.sqlservermonitoring.ui.theme.SqlServerMonitoringTheme
import org.jetbrains.jewel.bridge.addComposeTab

class SqlServerMonitoringWindowFactory : ToolWindowFactory {
    override fun shouldBeAvailable(project: Project) = true

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val localSource = DataSourceLocalSource()
        val monitorSource = MonitoringLocalSource()

        val monitorMapper = MonitorMapper()

        val dataSourceRepository = DataSourceImplRepository(localSource)
        val monitoringRepository = MonitoringImplRepository(monitorSource, monitorMapper)

        val getDataSources = GetDataSourcesUseCase(dataSourceRepository)
        val getMonitor = GetMonitorUseCase(monitoringRepository)

        val monitoringInteractor = MonitoringInteractor(
            getDataSources = getDataSources,
            getMonitor = getMonitor
        )

        val monitoringViewModel = MonitoringTabViewModel(monitoringInteractor)
        val monitorViewModel = MonitorViewModel(monitoringInteractor)

        toolWindow.addComposeTab(MyMessageBundle.message("tab.overview"), focusOnClickInside = true) {
            SqlServerMonitoringTheme {
                LaunchedEffect(Unit) {
                }
                MonitoringTab(project, monitoringViewModel, monitorViewModel)
            }
        }
    }
}