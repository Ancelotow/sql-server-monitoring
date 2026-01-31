package org.ancelotow.sqlservermonitoring.ui.tabs.monitoring_tab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.intellij.openapi.project.Project
import org.ancelotow.sqlservermonitoring.ui.models.DefaultStateStatus
import org.ancelotow.sqlservermonitoring.ui.theme.LocalColors
import org.ancelotow.sqlservermonitoring.ui.theme.LocalDimens
import org.ancelotow.sqlservermonitoring.ui.theme.MyMessageBundle
import org.ancelotow.sqlservermonitoring.ui.widgets.DataSourceComboBox
import org.ancelotow.sqlservermonitoring.ui.widgets.MonitoringPanelWidget
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.IconButton
import org.jetbrains.jewel.ui.icons.AllIconsKeys

@Composable
fun MonitoringTab(
    project: Project,
    viewModel: MonitoringTabViewModel
) {
    val state = viewModel.state
    LaunchedEffect(Unit) {
        viewModel.onEvent(MonitoringTabEvent.FetchSources(project))
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(LocalDimens.current.smallPadding),
        modifier = Modifier.padding(LocalDimens.current.largePadding)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(LocalDimens.current.smallPadding)
        ) {
            IconButton(
                onClick = { viewModel.onEvent(MonitoringTabEvent.FetchSources(project)) },
                enabled = state.status != DefaultStateStatus.LOADING
            ) {
                Icon(
                    key = AllIconsKeys.Actions.Refresh,
                    contentDescription = MyMessageBundle.message("icon.refresh"),
                    tint = LocalColors.current.greenColor
                )
            }

            DataSourceComboBox(
                isLoading = state.status == DefaultStateStatus.LOADING,
                items = state.sources,
                onItemSelected = { source -> viewModel.onEvent(MonitoringTabEvent.SelectSource(source)) },
                selected = viewModel.selectedDataSource
            )
        }
        Column {

            MonitoringPanelWidget(
                refreshMs = 5000,
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