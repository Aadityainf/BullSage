package com.bullsage.android.ui.navigation

import androidx.navigation.NavController

fun NavController.navigateToAuth() {
    navigate(BullSageDestinations.Destination.Auth.route)
}

fun NavController.navigateToSignUp() {
    navigate(BullSageDestinations.Destination.Auth.SignUp.route)
}

fun NavController.navigateToHome() {
    navigate(BullSageDestinations.BottomBarDestination.HOME.name) {
        popUpTo(BullSageDestinations.Destination.Onboarding.route) {
            inclusive = true
        }
    }
}

fun NavController.navigateOnSignOut() {
    navigate(BullSageDestinations.Destination.Onboarding.route){
        popUpTo(BullSageDestinations.BottomBarDestination.HOME.route) {
            inclusive = true
        }
    }
}