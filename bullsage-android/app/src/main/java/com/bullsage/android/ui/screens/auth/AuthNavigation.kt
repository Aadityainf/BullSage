package com.bullsage.android.ui.screens.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bullsage.android.ui.navigation.BullSageDestinations

fun NavGraphBuilder.authScreen() {
    composable(
        route = BullSageDestinations.AUTH.route
    ) {
        AuthRoute()
    }
}