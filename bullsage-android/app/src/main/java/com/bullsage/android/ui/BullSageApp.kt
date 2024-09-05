package com.bullsage.android.ui

import androidx.compose.runtime.Composable
import com.bullsage.android.ui.navigation.BullSageNavigation

@Composable
fun BullSageApp() {
    BullSageNavigation(hasOnboarded = false)
}