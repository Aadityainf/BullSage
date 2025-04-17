package com.bullsage.android.ui.screens.auth.signup

import android.util.Patterns
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
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiState())
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

    fun signUp() {
        if (_uiState.value.email.isBlank() || _uiState.value.password.isBlank()) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    emailError = null,
                    passwordError = null,
                    signUpButtonEnabled = false
                )
            }

            val email = _uiState.value.email
            val password = _uiState.value.password

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                // email not valid
                _uiState.update {
                    it.copy(
                        emailError = "Invalid email",
                        signUpButtonEnabled = true
                    )
                }
                return@launch
            }

            if (password.length < 8) {
                _uiState.update {
                    it.copy(
                        passwordError = "Password must contain at least 8 characters",
                        signUpButtonEnabled = true
                    )
                }
                return@launch
            }

            val authResponse = authRepository.signUp(
                email = email,
                password = password
            )

            when (authResponse) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(signUpSuccessful = true)
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            signUpButtonEnabled = true,
                            errorMessage = authResponse.errorMessage
                        )
                    }
                }
            }
        }
    }
}