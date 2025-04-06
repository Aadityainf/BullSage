package com.bullsage.android.ui.screens.explore

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bullsage.android.R
import com.bullsage.android.data.model.NewsItem
import com.bullsage.android.ui.components.SearchAppBar
import com.bullsage.android.ui.components.previews.ComponentPreview
import com.bullsage.android.ui.components.previews.DayNightPreviews
import com.bullsage.android.ui.components.stock.StockNewsItem
import com.bullsage.android.util.Padding
import kotlinx.coroutines.launch

@Composable
fun ExploreRoute(
    navigateToDetails: (String) -> Unit,
    viewModel: ExploreViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchSuggestions by viewModel.searchSuggestions.collectAsStateWithLifecycle()
    val news by viewModel.news.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    ExploreScreen(
        searchQuery = searchQuery,
        searchSuggestions = searchSuggestions,
        news = news,
        errorMessage = errorMessage,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onSearchSuggestionClick = navigateToDetails,
        onClearSearch = viewModel::onClearSearch,
        onErrorShown = viewModel::onErrorShown
    )
}

@Composable
fun ExploreScreen(
    searchQuery: String,
    searchSuggestions: List<String>,
    news: List<NewsItem>,
    errorMessage: String?,
    onSearchQueryChange: (String) -> Unit,
    onSearchSuggestionClick: (String) -> Unit,
    onClearSearch: () -> Unit,
    onErrorShown: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    errorMessage?.let {
        scope.launch { snackbarHostState.showSnackbar(it) }
        onErrorShown()
    }

    var searchBarActive by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            SearchAppBar(
                searchQuery = searchQuery,
                searchSuggestions = searchSuggestions,
                searchBarActive = searchBarActive,
                onSearchQueryChange = onSearchQueryChange,
                onActiveChange = { searchBarActive = it },
                onClearSearch = onClearSearch,
                onClick = onSearchSuggestionClick
            )

            LazyColumn(
                contentPadding = PaddingValues(horizontal = Padding.horizontalPadding),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    Text(
                        text = stringResource(R.string.news),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    )
                }
                items(
                    items = news
                ) {
                    StockNewsItem(
                        title = it.title,
                        link = it.link,
                        thumbnail = it.thumbnail?.resolutions?.first()?.url
                    )
                }
            }
        }
    }
}

@DayNightPreviews
@Composable
private fun ExploreScreenPreview() {
    ComponentPreview {
        ExploreScreen(
            searchQuery = "",
            searchSuggestions = emptyList(),
            news = emptyList(),
            errorMessage = null,
            onSearchQueryChange = {},
            onClearSearch = {},
            onSearchSuggestionClick = {},
            onErrorShown = {}
        )
    }
}