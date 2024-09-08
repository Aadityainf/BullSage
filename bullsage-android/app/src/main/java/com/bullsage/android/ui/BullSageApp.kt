package com.bullsage.android.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.bullsage.android.ui.navigation.BullSageDestinations
import com.bullsage.android.ui.navigation.BullSageNavigation

@Composable
fun BullSageApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentDestination = backStackEntry?.destination
    val bottomBarDestinations = remember {
        BullSageDestinations.BottomBarDestination.entries
    }

    val showBottomBar by remember {
        derivedStateOf {
            bottomBarDestinations.any {
                backStackEntry?.destination?.route == it.name
            }
        }
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    destinations = bottomBarDestinations,
                    currentDestination = currentDestination,
                    onNavigateToDestination = navController::navigateToBottomBarDestination
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            BullSageNavigation(
                navController = navController,
                hasOnboarded = false
            )
        }
    }
}

@Composable
private fun BottomNavigationBar(
    destinations: List<BullSageDestinations.BottomBarDestination>,
    currentDestination: NavDestination?,
    onNavigateToDestination: (BullSageDestinations.BottomBarDestination) -> Unit
) {
    NavigationBar {
        destinations.forEach { destination ->
            val selected = currentDestination.isMainDestinationInHierarchy(destination)
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        imageVector = if (selected) {
                            destination.selectedIcon
                        } else {
                            destination.icon
                        },
                        contentDescription = null
                    )
                }
            )
        }
    }
}

private fun NavDestination?.isMainDestinationInHierarchy(
    destination: BullSageDestinations.BottomBarDestination
): Boolean {
    return this?.hierarchy?.any { it.route == destination.name } == true
}

private fun NavController.navigateToBottomBarDestination(
    destination: BullSageDestinations.BottomBarDestination
) {
    val navOptions = navOptions {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }

    when (destination) {
        BullSageDestinations.BottomBarDestination.HOME -> {
            navigate(BullSageDestinations.BottomBarDestination.HOME.name, navOptions)
        }

        BullSageDestinations.BottomBarDestination.EXPLORE -> {
            navigate(BullSageDestinations.BottomBarDestination.EXPLORE.name, navOptions)
        }

        BullSageDestinations.BottomBarDestination.PROFILE -> {
            navigate(BullSageDestinations.BottomBarDestination.PROFILE.name, navOptions)
        }
    }
}