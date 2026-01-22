package org.ancelotow.sqlservermonitoring

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import org.ancelotow.sqlservermonitoring.ui.tabs.MonitoringTab
import org.ancelotow.sqlservermonitoring.ui.theme.SqlServerMonitoringTheme
import org.jetbrains.jewel.bridge.addComposeTab
import org.jetbrains.jewel.ui.component.Text

class MyToolWindowFactory : ToolWindowFactory {
    override fun shouldBeAvailable(project: Project) = true

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        toolWindow.addComposeTab("SQL Server Monitoring", focusOnClickInside = true) {
            SqlServerMonitoringTheme {
                LaunchedEffect(Unit) {
                    // initial monitoring data loading
                }
                MonitoringTab()
            }
        }

        toolWindow.addComposeTab("Settings", focusOnClickInside = true) {
            SqlServerMonitoringTheme {
                SettingsTab()
            }
        }
    }
}


@Composable
private fun SettingsTab() {
    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Settings will go here (data source selection, refresh interval, etc.)")
    }
}