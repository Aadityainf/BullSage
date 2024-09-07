package com.bullsage.android.ui.components.previews

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.bullsage.android.ui.theme.BullSageTheme

@Composable
fun ComponentPreview(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    BullSageTheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor,
        content =  {
            Surface { content() }
        }
    )
}