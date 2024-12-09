package com.bullsage.android.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.bullsage.android.R

sealed interface BullSageDestinations {
    enum class BottomBarDestination(
        val selectedIcon: ImageVector,
        val icon: ImageVector,
        val route: String,
        @StringRes val destinationName: Int
    ) : BullSageDestinations {
        HOME(
            selectedIcon = Icons.Rounded.Home,
            icon = Icons.Outlined.Home,
            route = "home",
            destinationName = R.string.home
        ),
        EXPLORE(
            selectedIcon = Icons.Rounded.Search,
            icon = Icons.Outlined.Search,
            route = "explore",
            destinationName = R.string.explore
        ),
        PROFILE(
            selectedIcon = Icons.Rounded.Person,
            icon = Icons.Outlined.Person,
            route = "profile",
            destinationName = R.string.profile
        )
    }

    sealed interface Destination : BullSageDestinations {
        val route: String

        data object Auth : Destination {
            override val route: String = "auth"

            data object Onboarding : Destination {
                override val route: String = "onboarding"
            }

            data object SignIn : Destination {
                override val route: String = "auth/signin"
            }

            data object SignUp : Destination {
                override val route: String = "auth/signup"
            }
        }
    }
}