package org.ancelotow.sqlservermonitoring.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun SqlServerMonitoringTheme(
    content: @Composable () -> Unit
) {
    val colors = defaultColors
    val dimens = defaultDimens

    CompositionLocalProvider(
        LocalColors provides colors,
        LocalDimens provides dimens
    ) {
        content()
    }
}