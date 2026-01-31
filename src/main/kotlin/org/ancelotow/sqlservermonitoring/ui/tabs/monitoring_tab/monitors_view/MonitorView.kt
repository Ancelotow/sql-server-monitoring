package org.ancelotow.sqlservermonitoring.ui.tabs.monitoring_tab.monitors_view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.intellij.database.dataSource.LocalDataSource
import com.intellij.openapi.project.Project
import org.ancelotow.sqlservermonitoring.ui.models.DefaultStateStatus
import org.ancelotow.sqlservermonitoring.ui.tabs.monitoring_tab.MonitoringTabEvent
import org.ancelotow.sqlservermonitoring.ui.tabs.monitoring_tab.monitors_view.MonitorViewModel.Companion.CAPACITY
import org.ancelotow.sqlservermonitoring.ui.tabs.monitoring_tab.monitors_view.MonitorViewModel.Companion.MONITORING_INTERVAL_MS
import org.ancelotow.sqlservermonitoring.ui.theme.LocalColors
import org.ancelotow.sqlservermonitoring.ui.theme.LocalDimens
import org.ancelotow.sqlservermonitoring.ui.theme.MyMessageBundle
import org.ancelotow.sqlservermonitoring.ui.widgets.MonitoringPanelWidget
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.IconButton
import org.jetbrains.jewel.ui.icons.AllIconsKeys

@Composable
fun MonitorView(
    project: Project,
    localDataSource: LocalDataSource,
    viewModel: MonitorViewModel
){
    val state = viewModel.state
    LaunchedEffect(Unit) {
        viewModel.onEvent(MonitorViewEvent.StartFetching(project, localDataSource))
    }

    Column {
        Row {
            if(state.isMonitoring){
                IconButton(
                    onClick = { viewModel.onEvent(MonitorViewEvent.StopFetching) },
                    enabled = state.status != DefaultStateStatus.LOADING
                ) {
                    Icon(
                        key = AllIconsKeys.Run.Stop,
                        contentDescription = MyMessageBundle.message("icon.stop")
                    )
                }
            } else {
                IconButton(
                    onClick = { viewModel.onEvent(MonitorViewEvent.ResumeFetching(project, localDataSource)) },
                    enabled = state.status != DefaultStateStatus.LOADING
                ) {
                    Icon(
                        key = AllIconsKeys.Actions.RunAll,
                        contentDescription = MyMessageBundle.message("icon.resume")
                    )
                }
            }
        }

        MonitoringPanelWidget(
            refreshMs = MONITORING_INTERVAL_MS,
            capacity = CAPACITY,
            monitorColors = LocalColors.current.processTimeMonitor,
            label = MyMessageBundle.message("label.processor_timer"),
            unitMetric = "%",
            maxValue = 100f,
            stopPropagation = !state.isMonitoring,
            modifier = Modifier.weight(1f).padding(bottom = LocalDimens.current.smallPadding)
        ) {
            viewModel.state.monitor.processorTime.toFloat()
        }

        MonitoringPanelWidget(
            refreshMs = MONITORING_INTERVAL_MS,
            capacity = CAPACITY,
            monitorColors = LocalColors.current.waitingTasksMonitor,
            label = MyMessageBundle.message("label.waiting_tasks"),
            maxValue = 10f,
            stopPropagation = !state.isMonitoring,
            modifier = Modifier.weight(1f).padding(bottom = LocalDimens.current.smallPadding)
        ) {
            viewModel.state.monitor.waitingTask.toFloat()
        }

        MonitoringPanelWidget(
            refreshMs = MONITORING_INTERVAL_MS,
            capacity = CAPACITY,
            monitorColors = LocalColors.current.databaseIOMonitor,
            label = MyMessageBundle.message("label.database_io"),
            unitMetric = "MB/s",
            maxValue = 10f,
            stopPropagation = !state.isMonitoring,
            modifier = Modifier.weight(1f).padding(bottom = LocalDimens.current.smallPadding)
        ) {
            viewModel.state.monitor.databaseIO.toFloat()
        }

        MonitoringPanelWidget(
            refreshMs = MONITORING_INTERVAL_MS,
            capacity = CAPACITY,
            monitorColors = LocalColors.current.batchRequestsMonitor,
            label = MyMessageBundle.message("label.batch_requests"),
            maxValue = 10f,
            stopPropagation = !state.isMonitoring,
            modifier = Modifier.weight(1f).padding(bottom = LocalDimens.current.smallPadding)
        ) {
            viewModel.state.monitor.batchRequests.toFloat()
        }

        MonitoringPanelWidget(
            refreshMs = MONITORING_INTERVAL_MS,
            capacity = CAPACITY,
            monitorColors = LocalColors.current.tempDBLogMonitor,
            label = MyMessageBundle.message("label.temp_db"),
            maxValue = 20000f,
            unitMetric = "MB",
            stopPropagation = !state.isMonitoring,
            modifier = Modifier.weight(1f).padding(bottom = LocalDimens.current.smallPadding)
        ) {
            viewModel.state.monitor.tempDbDiskUsage.toFloat()
        }

        MonitoringPanelWidget(
            refreshMs = MONITORING_INTERVAL_MS,
            capacity = CAPACITY,
            monitorColors = LocalColors.current.ramUsageMonitor,
            label = MyMessageBundle.message("label.ram_usage"),
            maxValue = 10000f,
            unitMetric = "MB",
            stopPropagation = !state.isMonitoring,
            modifier = Modifier.weight(1f).padding(bottom = LocalDimens.current.smallPadding)
        ) {
            viewModel.state.monitor.ramUsage.toFloat()
        }
    }
}