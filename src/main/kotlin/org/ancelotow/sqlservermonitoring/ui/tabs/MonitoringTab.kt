package org.ancelotow.sqlservermonitoring.ui.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.ancelotow.sqlservermonitoring.ui.theme.LocalColors
import org.ancelotow.sqlservermonitoring.ui.widgets.MonitoringPanelWidget
import org.jetbrains.jewel.ui.component.OutlinedButton
import org.jetbrains.jewel.ui.component.Text
import kotlin.random.Random

@Composable
fun MonitoringTab() {
    val labelText = remember { mutableStateOf("Monitoring: not started") }

    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(labelText.value)

        OutlinedButton(onClick = {
            labelText.value = "Monitoring tick: " + Random(System.currentTimeMillis()).nextInt(1000)
        }) { Text("Refresh") }

        MonitoringPanelWidget(
            refreshMs = 500,
            capacity = 120,
            monitorColors = LocalColors.current.processorMonitor
        ) {
            readFakeCpu() // remplace par ta métrique réelle


        }
    }
}

fun readFakeCpu(): Float {
    return (20..90).random().toFloat()
}