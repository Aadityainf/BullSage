package com.bullsage.android.ui.screens.detail.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.time.LocalDateTime

data class StockData(
    val price: Float,
    val date: Int
)

val now = LocalDateTime.now()
fun getStartOfWeek() = now.with(DayOfWeek.MONDAY)

val stockPerformance: List<StockData> = listOf(
    StockData(
        price = 420f,
        date = getStartOfWeek().dayOfMonth
    ),
    StockData(
        price = 400f,
        date = getStartOfWeek().plusDays(1).dayOfMonth
    ),
    StockData(
        price = 410f,
        date = getStartOfWeek().plusDays(2).dayOfMonth
    ),
    StockData(
        price = 450f,
        date = getStartOfWeek().plusDays(3).dayOfMonth
    ),
    StockData(
        price = 407f,
        date = getStartOfWeek().plusDays(4).dayOfMonth
    )
)

@Composable
fun StockChart(
    modifier: Modifier = Modifier
) {
    val stockData = remember { stockPerformance }
    val stockDataSize = remember(stockData) { stockData.size }
    val upperValue = remember(stockData) {
        stockData.maxOfOrNull { it.price }?.plus(1) ?: 0f
    }
    val lowerValue = remember(stockData) {
        stockData.minOfOrNull { it.price } ?: 0f
    }

    val textMeasurer = rememberTextMeasurer()
    Canvas(
        modifier = modifier
    ) {
        val spacing = 100f
        val topSpacing = 25f
        val width = size.width
        val height = size.height

        // Draw prices
        val numberOfPrices = 4
        val graphHeight = height - spacing
        val priceStep = (upperValue - lowerValue) / numberOfPrices
        (0..numberOfPrices).forEach { i ->
            val measuredText = textMeasurer.measure(
                text = (lowerValue + i * priceStep).toString(),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp
                )
            )
            drawText(
                textLayoutResult = measuredText,
                topLeft = Offset(
                    x = 50f,
                    y = graphHeight - (i * graphHeight / numberOfPrices) - (measuredText.size.height / 2) + topSpacing,
                )
            )
        }

        // Draw time
        val xAxisStartPoint = spacing + 50f
        val graphWidth = width - xAxisStartPoint
        val spacePerTime = graphWidth / stockDataSize
        stockData.indices.forEach { i ->
            val time = stockData[i].date
            val measuredText = textMeasurer.measure(
                text = time.toString(),
                style = TextStyle(
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            )
            drawText(
                textLayoutResult = measuredText,
                topLeft = Offset(
                    x = xAxisStartPoint + (i * spacePerTime) - (measuredText.size.width / 2),
                    y = height - 30 - (measuredText.size.height / 2)
                )
            )
        }

        // Draw horizontal line
        (0..numberOfPrices).forEach { i ->
            drawLine(
                color = if (i == 0) {
                    Color.Black
                } else {
                    Color.Gray
                },
                start = Offset(
                    x = xAxisStartPoint,
                    y = graphHeight - (i * graphHeight / numberOfPrices) + topSpacing
                ),
                end = Offset(
                    x = xAxisStartPoint + width - 2 * spacing,
                    y = graphHeight - (i * graphHeight / numberOfPrices) + topSpacing
                )
            )
        }

        // Graph line
        val strokePath = Path().apply {
            (0 until stockData.size - 1).forEach { i ->
                val data = stockData[i]
                val nextData = stockData.getOrNull(i + 1) ?: stockData.last()
                val dataRatio = (data.price - lowerValue) / (upperValue - lowerValue)
                val nextDataRatio = (nextData.price - lowerValue) / (upperValue - lowerValue)

                val x1 = xAxisStartPoint + (i * spacePerTime)
                val y1 = graphHeight - (dataRatio * graphHeight)
                val x2 = xAxisStartPoint + ((i + 1) * spacePerTime)
                val y2 = graphHeight - (nextDataRatio * graphHeight)

                moveTo(x1, y1 + topSpacing)
                lineTo(x2, y2 + topSpacing)
            }
        }
        drawPath(
            path = strokePath,
            color = Color.Green,
            style = Stroke(
                width = 2.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StockChartPreview() {
    StockChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
    )
}