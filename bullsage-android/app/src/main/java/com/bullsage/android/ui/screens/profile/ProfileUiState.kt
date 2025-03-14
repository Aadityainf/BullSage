package com.bullsage.android.ui.screens.profile

data class ProfileUiState(
    val email: String = "",
    val errorMessage: String? = null,
    val signOutSuccessful: Boolean = false
)