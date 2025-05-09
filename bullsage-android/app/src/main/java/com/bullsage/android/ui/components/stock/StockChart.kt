package com.bullsage.android.ui.components.stock

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
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
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.max

//data class StockData(
//    val price: Float,
//    val date: Int
//)
//
//val now = LocalDateTime.now()
//fun getStartOfWeek() = now.with(DayOfWeek.MONDAY)
//
//val stockPerformance: List<StockData> = listOf(
//    StockData(
//        price = 420f,
//        date = getStartOfWeek().dayOfMonth
//    ),
//    StockData(
//        price = 400f,
//        date = getStartOfWeek().plusDays(1).dayOfMonth
//    ),
//    StockData(
//        price = 410f,
//        date = getStartOfWeek().plusDays(2).dayOfMonth
//    ),
//    StockData(
//        price = 450f,
//        date = getStartOfWeek().plusDays(3).dayOfMonth
//    ),
//    StockData(
//        price = 407f,
//        date = getStartOfWeek().plusDays(4).dayOfMonth
//    )
//)

private val inputFormat = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
private val outputFormat = DateTimeFormatter.ofPattern("EEE", Locale.ENGLISH)

@SuppressLint("DefaultLocale")
@Composable
fun StockChart(
    price: List<Float>,
    date: List<String>,
    modifier: Modifier = Modifier
) {
    val stockDataSize = remember(date) { date.size }
    val upperValue = remember(date) {
        price.maxOrNull() ?: 0f
    }
    val lowerValue = remember(date) {
        price.minOrNull() ?: 0f
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
        var maxPriceTextWidth = 0
        val numberOfPrices = 4
        val graphHeight = height - spacing
        val priceStep = (upperValue - lowerValue) / numberOfPrices
        (0..numberOfPrices).forEach { i ->
            val measuredText = textMeasurer.measure(
                text = String.format("%.2f", lowerValue + i * priceStep),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp
                )
            )
            drawText(
                textLayoutResult = measuredText,
                topLeft = Offset(
                    x = 20f,
                    y = graphHeight - (i * graphHeight / numberOfPrices) - (measuredText.size.height / 2) + topSpacing,
                )
            )
            maxPriceTextWidth = max(measuredText.size.width, maxPriceTextWidth)
        }

        // Draw time
        val xAxisStartPoint = spacing + maxPriceTextWidth - 50f
        val graphWidth = width - xAxisStartPoint
        val spacePerTime = graphWidth / stockDataSize
        date.forEachIndexed { index, date ->
            val timestamp = inputFormat.parse(date)
            val dayName = outputFormat.format(timestamp)
            val measuredText = textMeasurer.measure(
                text = dayName,
                style = TextStyle(
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            )

            drawText(
                textLayoutResult = measuredText,
                topLeft = Offset(
                    x = xAxisStartPoint + (index * spacePerTime) - (measuredText.size.width / 2),
                    y = height - 30 - (measuredText.size.height / 2)
                )
            )
        }

        // Draw vertical line
        drawLine(
            color = Color.Black,
            start = Offset(
                x = xAxisStartPoint,
                y = graphHeight + topSpacing
            ),
            end = Offset(
                x = xAxisStartPoint,
                y = topSpacing - 10f
            )
        )

        // Graph line
        var lastX = 0f
        val strokePath = Path().apply {
            (0 until date.size - 1).forEach { i ->
                val nextData = price.getOrNull(i + 1) ?: price.last()
                val dataRatio = (price[i] - lowerValue) / (upperValue - lowerValue)
                val nextDataRatio = (nextData - lowerValue) / (upperValue - lowerValue)

                val x1 = xAxisStartPoint + (i * spacePerTime)
                val y1 = graphHeight - (dataRatio * graphHeight)
                val x2 = xAxisStartPoint + ((i + 1) * spacePerTime)
                val y2 = graphHeight - (nextDataRatio * graphHeight)

                moveTo(x1, y1 + topSpacing)
                lineTo(x2, y2 + topSpacing)
                lastX = x2
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

        // Draw horizontal line
        drawLine(
            color = Color.Black,
            start = Offset(
                x = xAxisStartPoint,
                y = graphHeight + topSpacing
            ),
            end = Offset(
                x = xAxisStartPoint + lastX - 100f,
                y = graphHeight + topSpacing
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun StockChartPreview() {
//    StockChart(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(500.dp)
//    )
}