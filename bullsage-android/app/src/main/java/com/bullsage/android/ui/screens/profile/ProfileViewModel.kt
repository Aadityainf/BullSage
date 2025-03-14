package com.bullsage.android.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bullsage.android.data.auth.UserAuthManager
import com.bullsage.android.data.model.Result
import com.bullsage.android.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userAuthManager: UserAuthManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(email = userAuthManager.getAuthDetails()!!.email)
            }
        }
    }

    fun onSignOut() {
        viewModelScope.launch {
            val response = authRepository.signOut()

            when(response) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(signOutSuccessful = true)
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(errorMessage = response.errorMessage)
                    }
                }
            }
        }
    }

    fun onErrorShown() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }
}