package com.bullsage.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.bullsage.android.ui.BullSageApp
import com.bullsage.android.ui.theme.BullSageTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BullSageTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    BullSageApp()
                }
            }
        }
    }
}