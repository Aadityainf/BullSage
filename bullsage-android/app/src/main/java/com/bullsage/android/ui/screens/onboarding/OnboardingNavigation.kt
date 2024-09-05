package com.bullsage.android.ui.screens.onboarding

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bullsage.android.ui.navigation.BullSageDestinations

fun NavGraphBuilder.onboardingScreen() {
    composable(
        route = BullSageDestinations.ONBOARDING.route
    ) {
        OnboardingRoute()
    }
}