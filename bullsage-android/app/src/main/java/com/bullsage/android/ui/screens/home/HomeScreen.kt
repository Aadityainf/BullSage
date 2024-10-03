package com.bullsage.android.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bullsage.android.R
import com.bullsage.android.ui.components.previews.ComponentPreview
import com.bullsage.android.ui.components.previews.DayNightPreviews
import com.bullsage.android.ui.components.stock.StockItem
import com.bullsage.android.ui.components.stock.StockPriceChip
import com.bullsage.android.util.Padding

@Composable
fun HomeRoute(

) {
    HomeScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(

) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(horizontal = Padding.horizontalPadding),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            marketMovers()
            watchlist()
        }
    }
}

private fun LazyListScope.marketMovers() {
    item {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            Text(
                text = stringResource(R.string.market_movers),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(
                    items = (1..10).map { it }
                ) {
                    StockPriceChip(1.2)
                }
            }
        }
    }
}

private fun LazyListScope.watchlist() {
    item {
        Text(
            text = stringResource(R.string.your_watchlist),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )
    }
    items(
        items = (1..4).map { it }
    ) {
        StockItem(1.62)
    }
}

@DayNightPreviews
@Composable
private fun HomeScreenPreview() {
    ComponentPreview {
        HomeScreen()
    }
}