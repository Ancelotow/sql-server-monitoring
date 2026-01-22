package org.ancelotow.sqlservermonitoring.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class MonitorColors(
    val backgroundColor: Color,
    val lineColor: Color
)

@Immutable
data class AppColors(
    val processorMonitor: MonitorColors,
    val labelColor: Color
)


val defaultMonitorColors = MonitorColors(
    backgroundColor = Color(0x55BC84F5),
    lineColor = Color(0xFFFF3BFF)
)

val defaultColors = AppColors(
    processorMonitor = defaultMonitorColors,
    labelColor = Color.Black
)

val LocalColors = staticCompositionLocalOf<AppColors> {
    error("No colors provided")
}