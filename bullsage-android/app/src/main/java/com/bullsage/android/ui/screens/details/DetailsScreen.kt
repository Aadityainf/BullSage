package com.bullsage.android.ui.screens.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bullsage.android.data.model.StockInfoResponse
import com.bullsage.android.ui.components.BackButton
import com.bullsage.android.ui.components.stock.StockChart
import kotlinx.coroutines.launch

@Composable
fun DetailsRoute(
    onBackClick: () -> Unit,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val stockDetails by viewModel.stockDetails.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    DetailScreen(
        ticker = viewModel.ticker,
        stockDetails = stockDetails,
        errorMessage = errorMessage,
        onAddToWatchlistClick = viewModel::addToWatchlist,
        onRemoveFromWatchlistClick = viewModel::removeFromWatchlist,
        onErrorShown = viewModel::onErrorShown,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    ticker: String?,
    stockDetails: StockDetails?,
    errorMessage: String?,
    onAddToWatchlistClick: () -> Unit,
    onRemoveFromWatchlistClick: () -> Unit,
    onErrorShown: () -> Unit,
    onBackClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    errorMessage?.let {
        scope.launch { snackbarHostState.showSnackbar(it) }
        onErrorShown()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(ticker ?: "")
                },
                navigationIcon = {
                    BackButton { onBackClick() }
                },
                actions = {
                    IconButton(
                        onClick = when (stockDetails?.isSaved) {
                            true -> onRemoveFromWatchlistClick
                            false -> onAddToWatchlistClick
                            else -> { {} }
                        }
                    ) {
                        if (stockDetails != null) {
                            Icon(
                                imageVector = if (stockDetails.isSaved) {
                                    Icons.Default.BookmarkAdded
                                } else Icons.Default.BookmarkBorder,
                                contentDescription = null
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (stockDetails != null) {
                LazyColumn(Modifier.fillMaxSize()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Text(text = stockDetails.info.longName)
                            Text(
                                text = "${stockDetails.info.currentPrice} ${stockDetails.info.currency}",
                                fontWeight = FontWeight.Bold
                            )
                            Text(text = "Predicted price - ${stockDetails.price.predicted}")
                        }
                    }
                    item {
                        StockChart(
                            price = stockDetails.price.close,
                            date = stockDetails.price.dates,
                            modifier = Modifier
                                .height(300.dp)
                                .fillMaxWidth()
                        )
                    }
                    stockInfoItems(stockDetails.info)
                }
            }
        }
    }
}

private fun LazyListScope.stockInfoItems(
    stockInfo: StockInfoResponse
) {
    item {
        StockInfoItem(
            name = "Prev Close",
            value = stockInfo.previousClose
        )
    }
    item {
        StockInfoItem(
            name = "Open",
            value = stockInfo.open
        )
    }
    item {
        StockInfoItem(
            name = "Volume",
            value = stockInfo.volume
        )
    }
    item {
        StockInfoItem(
            name = "Avg Vol (3m)",
            value = stockInfo.averageVolume
        )
    }
    item {
        StockInfoItem(
            name = "P/E",
            value = stockInfo.trailingPE
        )
    }
    item {
        StockInfoItem(
            name = "Mkt Cap",
            value = stockInfo.marketCap
        )
    }
    item {
        StockInfoItem(
            name = "Beta",
            value = stockInfo.beta
        )
    }
    item {
        StockInfoItem(
            name = "Forward Dividend & Yield",
            value = stockInfo.forwardDividendAndYield
        )
    }
    item {
        StockInfoItem(
            name = "Earnings",
            value = stockInfo.earnings
        )
    }
    item {
        StockInfoItem(
            name = "1y Target Est",
            value = stockInfo.targetMeanPrice
        )
    }
}

@Composable
fun StockInfoItem(
    name: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(name)
        Text(text = value)
    }
}