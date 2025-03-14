package com.bullsage.android.ui.components.stock

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bullsage.android.ui.components.previews.ComponentPreview
import com.bullsage.android.ui.components.previews.DayNightPreviews
import com.bullsage.android.util.Padding

@Composable
fun StockItem(
    name: String,
    symbol: String,
    onClick: (String) -> Unit,
    percentageChange: Float = 0f
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick(symbol) }
                .padding(PaddingValues(Padding.contentPadding))
        ) {
            Column {
                Text(
                    text = symbol,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

//            Column {
//                Text(
//                    text = "${123.45}",
//                    fontWeight = FontWeight.SemiBold,
//                    style = MaterialTheme.typography.bodyMedium,
//                )
//                Text(
//                    text = if (percentageChange < 0) {
//                        "${percentageChange}%"
//                    } else {
//                        "+${percentageChange}%"
//                    },
//                    color = if (percentageChange < 0) {
//                        Color.Red
//                    } else {
//                        MaterialTheme.colorScheme.primary
//                    },
//                    style = MaterialTheme.typography.bodyMedium,
//                )
//            }
        }
    }
}

@DayNightPreviews
@Composable
private fun StockItemPreview() {
    ComponentPreview {
        StockItem(
            name = "name",
            symbol = "SYM",
            onClick = {},
            percentageChange = 1.62f
        )
    }
}