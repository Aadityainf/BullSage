package com.bullsage.android.ui.components.stock

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bullsage.android.data.model.StockResponse
import com.bullsage.android.ui.components.previews.ComponentPreview
import com.bullsage.android.ui.components.previews.DayNightPreviews
import com.bullsage.android.util.Padding

@Composable
fun StockPriceChip(
    stock: StockResponse,
    onClick: (String) -> Unit
) {
    Card(
        border = BorderStroke(
            width = Dp.Hairline,
            color = Color.LightGray.copy(alpha = 0.5f)
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        modifier = Modifier.width(150.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick(stock.symbol) }
                .padding(PaddingValues(Padding.contentPadding))
        ) {
            Text(
                text = stock.symbol,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stock.name,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(Modifier.fillMaxWidth()) {
                Text(
                    text = "${stock.latestClose}",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = if (stock.percentChange < 0) {
                        "${stock.percentChange}%"
                    } else {
                        "+${stock.percentChange}%"
                    },
                    color = if (stock.percentChange < 0) {
                        Color.Red
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@DayNightPreviews
@Composable
private fun StockPriceChipPreview() {
    ComponentPreview {
        StockPriceChip(
            stock = StockResponse("", "HELLO",1.0, 1.0),
            onClick = {}
        )
    }
}