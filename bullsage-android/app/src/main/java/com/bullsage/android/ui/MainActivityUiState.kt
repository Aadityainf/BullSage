package com.bullsage.android.ui

sealed interface MainActivityUiState {
    data object Loading: MainActivityUiState

    data class Success(
        val userAuthenticated: Boolean
    ) : MainActivityUiState
}