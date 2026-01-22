package org.ancelotow.sqlservermonitoring.ui.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import org.ancelotow.sqlservermonitoring.ui.theme.LocalColors
import org.ancelotow.sqlservermonitoring.ui.theme.LocalDimens
import org.ancelotow.sqlservermonitoring.ui.theme.MonitorColors
import org.ancelotow.sqlservermonitoring.ui.theme.MyMessageBundle
import org.jetbrains.jewel.ui.component.Text
import kotlin.math.max
import kotlin.math.min


@Composable
fun MonitoringPanelWidget(
    modifier: Modifier = Modifier,
    monitorColors: MonitorColors,
    refreshMs: Long = 500L,
    capacity: Int = 120,
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
        val label = MyMessageBundle.message("label.processor_timer")
        val value = " %.2f%%".format(readMetric())

        Text(
            text = buildAnnotatedString {
                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                append(label)
                pushStyle(SpanStyle(fontWeight = FontWeight.Normal))
                append(value)
                pop()
            },
            color = LocalColors.current.labelColor,
            modifier = Modifier.padding(LocalDimens.current.padding)
        )
        LineGraph(
            data = values,
            capacity = 50,
            lineColor = monitorColors.lineColor
        )
    }
}

@Composable
private fun LineGraph(
    data: List<Float>,
    capacity: Int = 50,
    lineColor: Color,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        val width = size.width
        val height = size.height

        if (data.size < 2) return@Canvas

        val maxValue = max(data.maxOrNull() ?: 1f, 1f)
        val minValue = min(data.minOrNull() ?: 0f, maxValue - 1f)

        val points = data.takeLast(capacity).mapIndexed { index, value ->
            val x = width * index / (capacity - 1)
            val norm = (value - minValue) / (maxValue - minValue).coerceAtLeast(0.0001f)
            val y = height * (1 - norm)
            Offset(x, y)
        }

        val fillPath = Path().apply {
            moveTo(points.first().x, size.height)
            points.forEach { lineTo(it.x, it.y) }
            lineTo(points.last().x, size.height)
            close()
        }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    lineColor.copy(alpha = 0.25f),
                    lineColor.copy(alpha = 0.05f)
                ),
                endY = size.height
            )
        )


        for (i in 0 until points.lastIndex) {
            drawLine(
                color = lineColor,
                start = points[i],
                end = points[i + 1],
                strokeWidth = 2f,
                cap = StrokeCap.Round
            )
        }
    }
}

class TimeSeriesBuffer(private val capacity: Int) {

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

    fun isEmpty(): Boolean = size == 0
}