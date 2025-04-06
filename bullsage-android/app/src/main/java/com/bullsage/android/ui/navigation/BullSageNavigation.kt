package com.bullsage.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.bullsage.android.ui.screens.auth.signin.SignInRoute
import com.bullsage.android.ui.screens.auth.signup.SignUpRoute
import com.bullsage.android.ui.screens.details.DetailsRoute
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
        navigation(
            route = BullSageDestinations.Destination.Auth.route,
            startDestination = BullSageDestinations.Destination.Auth.Onboarding.route
        ) {
            composable(
                route = BullSageDestinations.Destination.Auth.Onboarding.route
            ) {
                OnboardingRoute(
                    onSignInClick = navController::navigateToSignIn,
                    onSignUpClick = navController::navigateToSignUp
                )
            }

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
                    onContinueClick = navController::navigateToHome,
                    onBackClick = navController::navigateUp
                )
            }
        }

        composable(
            route = BullSageDestinations.BottomBarDestination.HOME.route
        ) {
            HomeRoute(
                onClick = { navController.navigate("detail/$it") },
                navigateToAuth = navController::navigateOnSignOut
            )
        }

        composable(
            route = "detail/{ticker}",
            arguments = listOf(navArgument("ticker") { type = NavType.StringType })
        ) {
            DetailsRoute(onBackClick = navController::navigateUp)
        }

        composable(
            route = BullSageDestinations.BottomBarDestination.EXPLORE.route
        ) {
            ExploreRoute(
                navigateToDetails = { navController.navigate("detail/$it") }
            )
        }

        composable(
            route = BullSageDestinations.BottomBarDestination.PROFILE.route
        ) {
            ProfileRoute(
                onSignOutSuccessful = navController::navigateOnSignOut
            )
        }
    }
}