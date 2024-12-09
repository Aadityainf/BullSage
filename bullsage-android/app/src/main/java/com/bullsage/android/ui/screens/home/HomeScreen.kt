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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bullsage.android.R
import com.bullsage.android.data.model.StockResponse
import com.bullsage.android.ui.components.previews.ComponentPreview
import com.bullsage.android.ui.components.previews.DayNightPreviews
import com.bullsage.android.ui.components.stock.StockItem
import com.bullsage.android.ui.components.stock.StockPriceChip
import com.bullsage.android.util.Padding
import kotlinx.coroutines.launch

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        uiState = uiState,
        onErrorShown = viewModel::clearErrorMessage
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    uiState: HomeUiState,
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
                    marketMovers(movements = uiState.recentMovements)
                    watchlist()
                }
            }
        }
    }
}

private fun LazyListScope.marketMovers(
    movements: List<StockResponse>
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
                    StockPriceChip(stock)
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
        HomeScreen(
            uiState = HomeUiState.NotLoading(),
            onErrorShown = {}
        )
    }
}