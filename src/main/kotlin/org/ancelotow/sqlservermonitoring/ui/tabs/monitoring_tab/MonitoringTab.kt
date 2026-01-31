package org.ancelotow.sqlservermonitoring.ui.tabs.monitoring_tab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.intellij.openapi.project.Project
import org.ancelotow.sqlservermonitoring.ui.models.DefaultStateStatus
import org.ancelotow.sqlservermonitoring.ui.tabs.monitoring_tab.monitors_view.MonitorView
import org.ancelotow.sqlservermonitoring.ui.tabs.monitoring_tab.monitors_view.MonitorViewModel
import org.ancelotow.sqlservermonitoring.ui.theme.LocalColors
import org.ancelotow.sqlservermonitoring.ui.theme.LocalDimens
import org.ancelotow.sqlservermonitoring.ui.theme.MyMessageBundle
import org.ancelotow.sqlservermonitoring.ui.widgets.DataSourceComboBox
import org.ancelotow.sqlservermonitoring.ui.widgets.MonitoringPanelWidget
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.IconButton
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icons.AllIconsKeys

@Composable
fun MonitoringTab(
    project: Project,
    viewModel: MonitoringTabViewModel,
    monitorViewModel: MonitorViewModel
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
                onItemSelected = { source ->  viewModel.onEvent(MonitoringTabEvent.SelectSource(source)) },
                selected = state.sourceSelected
            )
        }
        if (state.sourceSelected != null) {
            MonitorView(
                project = project,
                localDataSource = state.sourceSelected,
                viewModel = monitorViewModel
            )
        } else {
            Text(
                MyMessageBundle.message("placeholder.no_datasource_selected"),
                modifier = Modifier.fillMaxWidth().padding(LocalDimens.current.smallPadding)
            )
        }
    }
}