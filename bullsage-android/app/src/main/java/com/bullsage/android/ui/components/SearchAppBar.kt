package com.bullsage.android.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bullsage.android.R
import com.bullsage.android.ui.components.previews.ComponentPreview
import com.bullsage.android.ui.components.previews.DayNightPreviews
import com.bullsage.android.util.Padding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    searchQuery: String,
    searchSuggestions: List<String>,
    searchBarActive: Boolean,
    onSearchQueryChange: (String) -> Unit,
    onActiveChange: (Boolean) -> Unit,
    onClearSearch: () -> Unit,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val searchBarHorizontalPadding by animateDpAsState(
        targetValue = if (searchBarActive) 0.dp else Padding.horizontalPadding,
        label = stringResource(R.string.search_bar_horizontal_padding)
    )
    val searchBarVerticalPadding by animateDpAsState(
        targetValue = if (searchBarActive) 0.dp else 8.dp,
        label = stringResource(R.string.search_bar_vertical_padding)
    )

    LaunchedEffect(searchBarActive) {
        if (!searchBarActive) {
            onClearSearch()
        }
    }

    SearchBar(
        query = if (searchBarActive) searchQuery else "",
        onQueryChange = onSearchQueryChange,
        onSearch = {},
        active = searchBarActive,
        onActiveChange = onActiveChange,
        leadingIcon = {
            if (searchBarActive) {
                BackButton { onActiveChange(false) }
            } else {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = stringResource(R.string.search)
                )
            }
        },
        trailingIcon = {
            if (searchQuery.isNotBlank() || searchQuery.isNotEmpty()) {
                IconButton(onClick = onClearSearch) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.clear_search)
                    )
                }
            }
        },
        placeholder = {
            Text(text = stringResource(id = R.string.search))
        },
        windowInsets = WindowInsets(top = 0.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = searchBarHorizontalPadding,
                vertical = searchBarVerticalPadding
            )
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = Padding.horizontalPadding),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(
                items = searchSuggestions
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onClick(it) }
                        .padding(10.dp)
                ) {
                    Text(it)
                }
            }
        }
    }

}

@DayNightPreviews
@Composable
private fun SearchBarPreview() {
    ComponentPreview {
        SearchAppBar(
            searchQuery = "",
            searchSuggestions = emptyList(),
            searchBarActive = false,
            onActiveChange = {},
            onSearchQueryChange = {},
            onClearSearch = {},
            onClick = {}
        )
    }
}