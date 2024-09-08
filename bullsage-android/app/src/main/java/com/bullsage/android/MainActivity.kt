package com.bullsage.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.bullsage.android.ui.BullSageApp
import com.bullsage.android.ui.theme.BullSageTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BullSageTheme {
                BullSageApp()
            }
        }
    }
}