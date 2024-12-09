package com.bullsage.android.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bullsage.android.data.model.Result
import com.bullsage.android.data.model.StockResponse
import com.bullsage.android.data.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val stockRepository: StockRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var recentMovements: List<StockResponse> = emptyList()

    init {
        loadData()
    }

    fun clearErrorMessage() {
        _uiState.update {
            (it as HomeUiState.NotLoading).copy(errorMessage = null)
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                getRecentStockMovements()

                _uiState.update {
                    HomeUiState.Success(
                        recentMovements = recentMovements
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    HomeUiState.NotLoading(errorMessage = e.message)
                }
            }
        }
    }

    private suspend fun getRecentStockMovements() {
        when (val response = stockRepository.getRecentMovements()) {
            is Result.Success -> recentMovements = response.data

            is Result.Error -> throw Exception(response.errorMessage)
        }
    }
}