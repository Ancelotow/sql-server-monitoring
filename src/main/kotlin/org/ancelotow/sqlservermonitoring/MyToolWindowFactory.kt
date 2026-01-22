package org.ancelotow.sqlservermonitoring

import androidx.compose.runtime.LaunchedEffect
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import org.ancelotow.sqlservermonitoring.ui.tabs.MonitoringTab
import org.ancelotow.sqlservermonitoring.ui.theme.MyMessageBundle
import org.ancelotow.sqlservermonitoring.ui.theme.SqlServerMonitoringTheme
import org.jetbrains.jewel.bridge.addComposeTab

class MyToolWindowFactory : ToolWindowFactory {
    override fun shouldBeAvailable(project: Project) = true

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        toolWindow.addComposeTab(MyMessageBundle.message("tab.overview"), focusOnClickInside = true) {
            SqlServerMonitoringTheme {
                LaunchedEffect(Unit) {
                }
                MonitoringTab()
            }
        }

    }
}