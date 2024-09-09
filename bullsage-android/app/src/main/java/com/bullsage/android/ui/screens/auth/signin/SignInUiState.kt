package com.bullsage.android.ui.screens.auth.signin

data class SignInUiState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val signInButtonEnabled: Boolean = true,
    val errorMessage: String? = null,
    val signInSuccessful: Boolean = false
)