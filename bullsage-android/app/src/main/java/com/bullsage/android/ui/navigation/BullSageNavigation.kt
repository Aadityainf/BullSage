package com.bullsage.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.bullsage.android.ui.screens.auth.authScreen
import com.bullsage.android.ui.screens.explore.exploreScreen
import com.bullsage.android.ui.screens.home.homeScreen
import com.bullsage.android.ui.screens.onboarding.onboardingScreen
import com.bullsage.android.ui.screens.profile.profileScreen

@Composable
fun BullSageNavigation(
    navController: NavHostController = rememberNavController(),
    hasOnboarded: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = if (hasOnboarded) {
            BullSageDestinations.HOME.route
        } else {
            BullSageDestinations.ONBOARDING.route
        }
    ) {
        onboardingScreen()
        authScreen()
        homeScreen()
        exploreScreen()
        profileScreen()
    }
}