package com.bullsage.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.bullsage.android.ui.screens.auth.signin.SignInRoute
import com.bullsage.android.ui.screens.auth.signup.SignUpRoute
import com.bullsage.android.ui.screens.explore.ExploreRoute
import com.bullsage.android.ui.screens.home.HomeRoute
import com.bullsage.android.ui.screens.onboarding.OnboardingRoute
import com.bullsage.android.ui.screens.profile.ProfileRoute

@Composable
fun BullSageNavigation(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            route = BullSageDestinations.Destination.Onboarding.route
        ) {
            OnboardingRoute(
                onGetStartedClick = navController::navigateToAuth
            )
        }

        navigation(
            route = BullSageDestinations.Destination.Auth.route,
            startDestination = BullSageDestinations.Destination.Auth.SignIn.route
        ) {
            composable(
                route = BullSageDestinations.Destination.Auth.SignIn.route
            ) {
                SignInRoute(
                    onSignUpClick = navController::navigateToSignUp,
                    onSignInSuccessful = navController::navigateToHome,
                    onBackClick = navController::navigateUp
                )
            }
            composable(
                route = BullSageDestinations.Destination.Auth.SignUp.route
            ) {
                SignUpRoute(
                    onContinueClick = {},
                    onBackClick = navController::navigateUp
                )
            }
        }

        composable(
            route = BullSageDestinations.BottomBarDestination.HOME.route
        ) {
            HomeRoute()
        }

        composable(
            route = BullSageDestinations.BottomBarDestination.EXPLORE.route
        ) {
            ExploreRoute()
        }

        composable(
            route = BullSageDestinations.BottomBarDestination.PROFILE.route
        ) {
            ProfileRoute()
        }
    }
}