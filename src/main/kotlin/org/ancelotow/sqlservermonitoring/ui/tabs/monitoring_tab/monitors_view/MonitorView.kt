package org.ancelotow.sqlservermonitoring.ui.tabs.monitoring_tab.monitors_view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.intellij.database.dataSource.LocalDataSource
import com.intellij.openapi.project.Project
import org.ancelotow.sqlservermonitoring.ui.tabs.monitoring_tab.MonitoringTabViewModel
import org.ancelotow.sqlservermonitoring.ui.tabs.monitoring_tab.monitors_view.MonitorViewModel.Companion.CAPACITY
import org.ancelotow.sqlservermonitoring.ui.tabs.monitoring_tab.monitors_view.MonitorViewModel.Companion.MONITORING_INTERVAL_MS
import org.ancelotow.sqlservermonitoring.ui.theme.LocalColors
import org.ancelotow.sqlservermonitoring.ui.theme.LocalDimens
import org.ancelotow.sqlservermonitoring.ui.theme.MyMessageBundle
import org.ancelotow.sqlservermonitoring.ui.widgets.MonitoringPanelWidget

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
        MonitoringPanelWidget(
            refreshMs = MONITORING_INTERVAL_MS,
            capacity = CAPACITY,
            monitorColors = LocalColors.current.processTimeMonitor,
            label = MyMessageBundle.message("label.processor_timer"),
            unitMetric = "%",
            modifier = Modifier.weight(1f).padding(bottom = LocalDimens.current.smallPadding)
        ) {
            viewModel.state.monitor.processorTime.toFloat()
        }

        MonitoringPanelWidget(
            refreshMs = MONITORING_INTERVAL_MS,
            capacity = CAPACITY,
            monitorColors = LocalColors.current.waitingTasksMonitor,
            label = MyMessageBundle.message("label.waiting_tasks"),
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
            modifier = Modifier.weight(1f).padding(bottom = LocalDimens.current.smallPadding)
        ) {
            viewModel.state.monitor.databaseIO.toFloat()
        }

        MonitoringPanelWidget(
            refreshMs = MONITORING_INTERVAL_MS,
            capacity = CAPACITY,
            monitorColors = LocalColors.current.batchRequestsMonitor,
            label = MyMessageBundle.message("label.batch_requests"),
            modifier = Modifier.weight(1f).padding(bottom = LocalDimens.current.smallPadding)
        ) {
            viewModel.state.monitor.batchRequests.toFloat()
        }
    }
}