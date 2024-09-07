package com.bullsage.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.bullsage.android.ui.screens.auth.signin.SignInRoute
import com.bullsage.android.ui.screens.auth.signup.SignUpRoute
import com.bullsage.android.ui.screens.explore.exploreScreen
import com.bullsage.android.ui.screens.home.homeScreen
import com.bullsage.android.ui.screens.onboarding.OnboardingRoute
import com.bullsage.android.ui.screens.profile.profileScreen

@Composable
fun BullSageNavigation(
    navController: NavHostController = rememberNavController(),
    hasOnboarded: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = if (hasOnboarded) {
            BullSageDestinations.BottomBarDestination.HOME.name
        } else {
            BullSageDestinations.Destination.Onboarding.route
        }
    ) {
        composable(
            route = BullSageDestinations.Destination.Onboarding.route
        ) {
            OnboardingRoute()
        }

        navigation(
            route = BullSageDestinations.Destination.Auth.route,
            startDestination = BullSageDestinations.Destination.Auth.SignIn.route
        ) {
            composable(
                route = BullSageDestinations.Destination.Auth.SignIn.route
            ) {
                SignInRoute()
            }
            composable(
                route = BullSageDestinations.Destination.Auth.SignUp.route
            ) {
                SignUpRoute()
            }
        }

        homeScreen()
        exploreScreen()
        profileScreen()
    }
}