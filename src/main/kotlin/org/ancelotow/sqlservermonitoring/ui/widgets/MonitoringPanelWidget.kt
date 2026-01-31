package org.ancelotow.sqlservermonitoring.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.zIndex
import org.ancelotow.sqlservermonitoring.ui.theme.LocalColors
import org.ancelotow.sqlservermonitoring.ui.theme.LocalDimens
import org.ancelotow.sqlservermonitoring.ui.theme.MonitorColors
import org.jetbrains.jewel.ui.component.Text


@Composable
fun MonitoringPanelWidget(
    modifier: Modifier = Modifier,
    monitorColors: MonitorColors,
    refreshMs: Long = 500L,
    capacity: Int = 120,
    label: String,
    unitMetric: String = "",
    readMetric: () -> Float
) {
    val buffer = remember { TimeSeriesBuffer(capacity) }
    var values by remember { mutableStateOf(emptyList<Float>()) }

    LaunchedEffect(Unit) {
        while (true) {
            val sample = readMetric()
            buffer.add(sample)
            values = buffer.values()
            delay(refreshMs)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = monitorColors.backgroundColor,
                shape = RoundedCornerShape(LocalDimens.current.radius)
            )
    ) {
        val value = " %.2f".format(readMetric())

        Text(
            text = buildAnnotatedString {
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                append(label)
                pushStyle(SpanStyle(fontWeight = FontWeight.Normal))
                append(value)
                append(" $unitMetric")
                pop()
            },
            color = LocalColors.current.labelColor,
            modifier = Modifier.padding(LocalDimens.current.padding).zIndex(1f)
        )
        LineGraph(
            data = values,
            capacity = 50,
            lineColor = monitorColors.lineColor
        )
    }
}

private class TimeSeriesBuffer(private val capacity: Int) {

    private val data = FloatArray(capacity)
    private var index = 0
    private var size = 0

    fun add(value: Float) {
        data[index] = value
        index = (index + 1) % capacity
        if (size < capacity) size++
    }

    fun values(): List<Float> {
        return (0..size).map {
            data[(index - size + it + capacity) % capacity]
        }
    }
}