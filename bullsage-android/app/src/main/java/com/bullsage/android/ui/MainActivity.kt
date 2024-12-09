package com.bullsage.android.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bullsage.android.ui.navigation.BullSageDestinations
import com.bullsage.android.ui.theme.BullSageTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    uiState = it
                }
            }
        }

        splashScreen.setKeepOnScreenCondition {
            uiState is MainActivityUiState.Loading
        }

        setContent {
            BullSageTheme {
                if (uiState is MainActivityUiState.Success) {
                    val authenticated = (uiState as MainActivityUiState.Success).userAuthenticated
                    BullSageApp(
                        startDestination = if (authenticated) {
                            BullSageDestinations.BottomBarDestination.HOME.route
                        } else {
                            BullSageDestinations.Destination.Auth.route
                        }
                    )
                }
            }
        }
    }
}