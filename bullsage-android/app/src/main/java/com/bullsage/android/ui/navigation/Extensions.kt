package com.bullsage.android.ui.navigation

import androidx.navigation.NavController

fun NavController.navigateToSignIn() {
    navigate(BullSageDestinations.Destination.Auth.SignIn.route)
}

fun NavController.navigateToSignUp() {
    navigate(BullSageDestinations.Destination.Auth.SignUp.route)
}

fun NavController.navigateToHome() {
    navigate(BullSageDestinations.BottomBarDestination.HOME.name) {
        popUpTo(BullSageDestinations.Destination.Auth.route) {
            inclusive = true
        }
    }
}

fun NavController.navigateOnSignOut() {
    navigate(BullSageDestinations.Destination.Auth.route){
        popUpTo(BullSageDestinations.BottomBarDestination.HOME.route) {
            inclusive = true
        }
    }
}