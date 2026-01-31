package org.ancelotow.sqlservermonitoring.ui.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import kotlin.math.max
import kotlin.math.min

@Composable
fun LineGraph(
    data: List<Float>,
    capacity: Int = 50,
    lineColor: Color,
    modifier: Modifier = Modifier,
    maxValue: Float = 100f
) {
    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        val width = size.width
        val height = size.height

        if (data.size < 2) return@Canvas

        val maxValue = max((data.maxOrNull() ?: 1f) + 10, maxValue)
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