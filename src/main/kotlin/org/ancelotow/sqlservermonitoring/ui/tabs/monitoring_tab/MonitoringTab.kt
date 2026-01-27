package org.ancelotow.sqlservermonitoring.ui.tabs.monitoring_tab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.intellij.openapi.project.Project
import org.ancelotow.sqlservermonitoring.ui.models.DefaultStateStatus
import org.ancelotow.sqlservermonitoring.ui.theme.LocalColors
import org.ancelotow.sqlservermonitoring.ui.theme.LocalDimens
import org.ancelotow.sqlservermonitoring.ui.theme.MyMessageBundle
import org.ancelotow.sqlservermonitoring.ui.widgets.DataSourceComboBox
import org.ancelotow.sqlservermonitoring.ui.widgets.MonitoringPanelWidget

@Composable
fun MonitoringTab(
    project: Project,
    viewModel: MonitoringTabViewModel
) {
    val state = viewModel.state
    LaunchedEffect(Unit) {
        viewModel.onEvent(MonitoringTabEvent.FetchSources(project))
    }

    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        DataSourceComboBox(
            isLoading = state.status == DefaultStateStatus.LOADING,
            items = state.sources,
            onItemSelected = { },
            selected = null
        )

        Column {

            MonitoringPanelWidget(
                refreshMs = 500,
                capacity = 120,
                monitorColors = LocalColors.current.processTimeMonitor,
                label = MyMessageBundle.message("label.processor_timer"),
                unitMetric = "%",
                modifier = Modifier.weight(1f).padding(bottom = LocalDimens.current.smallPadding)
            ) {
                readFakeCpu()
            }

            MonitoringPanelWidget(
                refreshMs = 500,
                capacity = 120,
                monitorColors = LocalColors.current.waitingTasksMonitor,
                label = MyMessageBundle.message("label.waiting_tasks"),
                modifier = Modifier.weight(1f).padding(bottom = LocalDimens.current.smallPadding)
            ) {
                readFakeCpu()
            }

            MonitoringPanelWidget(
                refreshMs = 500,
                capacity = 120,
                monitorColors = LocalColors.current.databaseIOMonitor,
                label = MyMessageBundle.message("label.database_io"),
                unitMetric = "MB/s",
                modifier = Modifier.weight(1f).padding(bottom = LocalDimens.current.smallPadding)
            ) {
                readFakeCpu()
            }

            MonitoringPanelWidget(
                refreshMs = 500,
                capacity = 120,
                monitorColors = LocalColors.current.batchRequestsMonitor,
                label = MyMessageBundle.message("label.batch_requests"),
                modifier = Modifier.weight(1f).padding(bottom = LocalDimens.current.smallPadding)
            ) {
                readFakeCpu()
            }
        }

    }
}

fun readFakeCpu(): Float {
    return (20..90).random().toFloat()
}