package com.bullsage.android.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bullsage.android.data.auth.UserAuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userAuthManager: UserAuthManager
) : ViewModel() {
    private val _uiState = MutableStateFlow<MainActivityUiState>(MainActivityUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        checkUserAuthStatus()
    }

    private fun checkUserAuthStatus() {
        viewModelScope.launch {
            if (userAuthManager.getAuthDetails() != null) {
                _uiState.update {
                    MainActivityUiState.Success(userAuthenticated = true)
                }
            } else {
                _uiState.update {
                    MainActivityUiState.Success(userAuthenticated = false)
                }
            }
        }
    }
}