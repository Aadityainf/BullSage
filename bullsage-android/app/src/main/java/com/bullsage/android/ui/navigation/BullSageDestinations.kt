package com.bullsage.android.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface BullSageDestinations {
    enum class BottomBarDestination(
        val selectedIcon: ImageVector,
        val icon: ImageVector
    ) : BullSageDestinations {
        HOME(
            selectedIcon = Icons.Rounded.Home,
            icon = Icons.Outlined.Home
        ),
        EXPLORE(
            selectedIcon = Icons.Rounded.Search,
            icon = Icons.Outlined.Search
        ),
        PROFILE(
            selectedIcon = Icons.Rounded.Person,
            icon = Icons.Outlined.Person
        )
    }

    sealed interface Destination : BullSageDestinations {
        val route: String

        data object Onboarding : Destination {
            override val route: String = "onboarding"
        }

        data object Auth : Destination {
            override val route: String = "auth"

            data object SignIn : Destination {
                override val route: String = "auth/signin"
            }

            data object SignUp : Destination {
                override val route: String = "auth/signup"
            }
        }
    }
}