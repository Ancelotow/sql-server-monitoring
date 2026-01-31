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
    val processTimeMonitor: MonitorColors,
    val waitingTasksMonitor: MonitorColors,
    val databaseIOMonitor: MonitorColors,
    val batchRequestsMonitor: MonitorColors,
    val ramUsageMonitor: MonitorColors,
    val tempDBLogMonitor: MonitorColors,
    val labelColor: Color,

    val greenColor: Color,
)
val defaultColors = AppColors(
    processTimeMonitor = MonitorColors(
        backgroundColor = Color(0x334CAF50),
        lineColor = Color(0xFF4CAF50)
    ),
    waitingTasksMonitor = MonitorColors(
        backgroundColor = Color(0x33FF9800),
        lineColor = Color(0xFFFF9800)
    ),
    databaseIOMonitor = MonitorColors(
        backgroundColor = Color(0x333F51B5),
        lineColor = Color(0xFF7C4DFF)
    ),
    batchRequestsMonitor = MonitorColors(
        backgroundColor = Color(0x33009688),
        lineColor = Color(0xFF009688)
    ),
    ramUsageMonitor = MonitorColors(
        backgroundColor = Color(0x33D32F2F),
        lineColor = Color(0xFFD32F2F)
    ),
    tempDBLogMonitor = MonitorColors(
        backgroundColor = Color(0x335E35B1),
        lineColor = Color(0xFF5E35B1)
    ),
    labelColor = Color.White,
    greenColor = Color(0xFF00FF00)
)

val LocalColors = staticCompositionLocalOf<AppColors> {
    error("No colors provided")
}