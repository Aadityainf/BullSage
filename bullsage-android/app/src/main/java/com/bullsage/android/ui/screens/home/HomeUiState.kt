package com.bullsage.android.ui.screens.home

import com.bullsage.android.data.model.StockResponse

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class NotLoading(
        val errorMessage: String? = null
    ) : HomeUiState

    data class Success(
        val recentMovements: List<StockResponse> = emptyList()
    ) : HomeUiState
}
