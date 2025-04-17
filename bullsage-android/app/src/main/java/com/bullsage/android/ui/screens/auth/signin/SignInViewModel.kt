package com.bullsage.android.ui.screens.auth.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bullsage.android.data.model.Result
import com.bullsage.android.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState = _uiState.asStateFlow()

    fun changeEmail(email: String) {
        _uiState.update {
            it.copy(email = email)
        }
    }

    fun changePassword(password: String) {
        _uiState.update {
            it.copy(password = password)
        }
    }

    fun changePasswordVisibility() {
        _uiState.update {
            it.copy(passwordVisible = !(_uiState.value.passwordVisible))
        }
    }

    fun errorShown() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }

    fun signIn() {
        if (_uiState.value.email.isBlank() || _uiState.value.password.isBlank()) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(signInButtonEnabled = false)
            }

            val authResponse = authRepository.signIn(
                email = _uiState.value.email,
                password = _uiState.value.password
            )

            when(authResponse) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(signInSuccessful = true)
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            signInButtonEnabled = true,
                            errorMessage = authResponse.errorMessage
                        )
                    }
                }
            }
        }
    }
}