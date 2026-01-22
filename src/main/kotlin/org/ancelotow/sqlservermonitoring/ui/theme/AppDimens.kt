package org.ancelotow.sqlservermonitoring.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class AppDimens(
    val padding: Dp,
    val smallPadding: Dp,
    val radius: Dp
)

val defaultDimens = AppDimens(
    padding = 16.dp,
    smallPadding = 8.dp,
    radius = 8.dp
)

val LocalDimens = staticCompositionLocalOf<AppDimens> {
    error("No dimens provided")
}