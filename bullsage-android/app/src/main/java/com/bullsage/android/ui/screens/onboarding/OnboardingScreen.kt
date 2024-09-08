package com.bullsage.android.ui.screens.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bullsage.android.R
import com.bullsage.android.ui.components.previews.ComponentPreview
import com.bullsage.android.ui.components.previews.DayNightPreviews

@Composable
fun OnboardingRoute(
    onGetStartedClick: () -> Unit
) {
    OnboardingScreen(
        onGetStartedClick = onGetStartedClick
    )
}

@Composable
fun OnboardingScreen(
    onGetStartedClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Column(
           modifier = Modifier
               .fillMaxWidth()
               .weight(1f)
        ) {

        }
        Button(
            onClick = onGetStartedClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(bottom = 10.dp)
        ) {
            Text(text = stringResource(id = R.string.get_started))
        }
    }
}

@DayNightPreviews
@Composable
private fun OnboardingScreenPreview() {
    ComponentPreview {
        OnboardingScreen(
            onGetStartedClick = {}
        )
    }
}