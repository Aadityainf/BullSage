package com.bullsage.android.ui.screens.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bullsage.android.data.model.NewsItem
import com.bullsage.android.data.model.Result
import com.bullsage.android.data.network.BullsageApi
import com.bullsage.android.data.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val stockRepository: StockRepository,
    bullsageApi: BullsageApi
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _news = MutableStateFlow<List<NewsItem>>(emptyList())
    val news = _news.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    val searchSuggestions: StateFlow<List<String>> = _searchQuery
        .debounce(200L)
        .mapLatest {
            if (it.isBlank()) emptyList()
            else when (val response = stockRepository.searchStock(it)) {
                is Result.Success -> response.data
                is Result.Error -> emptyList()
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            _news.update { bullsageApi.getNews().data }
        }
    }

    fun onSearchQueryChange(searchQuery: String) {
        _searchQuery.update { searchQuery }
    }

    fun onClearSearch() {
        _searchQuery.update { "" }
    }

    fun onErrorShown() {
        _errorMessage.update { null }
    }
}