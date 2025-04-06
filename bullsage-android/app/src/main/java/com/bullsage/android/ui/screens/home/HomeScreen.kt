package com.bullsage.android.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bullsage.android.R
import com.bullsage.android.data.db.WatchlistEntity
import com.bullsage.android.data.model.StockResponse
import com.bullsage.android.ui.components.previews.ComponentPreview
import com.bullsage.android.ui.components.previews.DayNightPreviews
import com.bullsage.android.ui.components.stock.StockItem
import com.bullsage.android.ui.components.stock.StockPriceChip
import com.bullsage.android.util.Padding
import kotlinx.coroutines.launch

@Composable
fun HomeRoute(
    onClick: (String) -> Unit,
    navigateToAuth: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val watchlist by viewModel.watchlistItems.collectAsStateWithLifecycle()
    val navigateToAuth by viewModel.navigateToAuth.collectAsStateWithLifecycle()

    LaunchedEffect(navigateToAuth) {
        if (navigateToAuth) {
            navigateToAuth()
        }
    }

    HomeScreen(
        uiState = uiState,
        watchlist = watchlist,
        onClick = onClick,
        onErrorShown = viewModel::clearErrorMessage
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    uiState: HomeUiState,
    watchlist: List<WatchlistEntity>,
    onClick: (String) -> Unit,
    onErrorShown: () -> Unit
) {
    val snackbarState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
            )
        }
    ) { innerPadding ->
        when (uiState) {
            is HomeUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
            }

            is HomeUiState.NotLoading -> {
                uiState.errorMessage?.let {
                    scope.launch { snackbarState.showSnackbar(it) }
                    onErrorShown()
                }
            }

            is HomeUiState.Success -> {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = Padding.horizontalPadding),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    marketMovers(
                        movements = uiState.recentMovements,
                        onClick = onClick
                    )
                    watchlist(
                        watchlist = watchlist,
                        onClick = onClick
                    )
                }
            }
        }
    }
}

private fun LazyListScope.marketMovers(
    movements: List<StockResponse>,
    onClick: (String) -> Unit
) {
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
                    items = movements
                ) { stock ->
                    StockPriceChip(
                        stock = stock,
                        onClick = onClick
                    )
                }
            }
        }
    }
}

private fun LazyListScope.watchlist(
    watchlist: List<WatchlistEntity>,
    onClick: (String) -> Unit
) {
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
    if (watchlist.isEmpty()) {
        item {
            Text(
                text = stringResource(id = R.string.empty_watchlist_text),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    } else {
        items(
            items = watchlist
        ) {
            StockItem(
                name = it.longName,
                symbol = it.ticker,
                onClick = onClick
            )
        }
    }
}

@DayNightPreviews
@Composable
private fun HomeScreenPreview() {
    ComponentPreview {
        HomeScreen(
            uiState = HomeUiState.NotLoading(),
            watchlist = emptyList(),
            onClick = {},
            onErrorShown = {}
        )
    }
}