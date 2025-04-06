package com.bullsage.android.ui.components.stock

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bullsage.android.ui.components.previews.ComponentPreview
import com.bullsage.android.ui.components.previews.DayNightPreviews
import com.bullsage.android.util.Padding

@Composable
fun StockNewsItem(
    title: String,
    link: String,
    thumbnail: String?
) {
    val uriHandler = LocalUriHandler.current
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { uriHandler.openUri(link) }
                .padding(PaddingValues(Padding.contentPadding))
        ) {
            AsyncImage(
                model = thumbnail,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
        }
    }
}

@DayNightPreviews
@Composable
private fun StockNewsItemPreview() {
    ComponentPreview {
        StockNewsItem("", "", "")
    }
}