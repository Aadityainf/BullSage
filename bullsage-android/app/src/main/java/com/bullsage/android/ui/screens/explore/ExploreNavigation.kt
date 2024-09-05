package com.bullsage.android.ui.screens.explore

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.bullsage.android.ui.navigation.BullSageDestinations

fun NavGraphBuilder.exploreScreen() {
    composable(
        route = BullSageDestinations.EXPLORE.route
    ) {
        ExploreRoute()
    }
}