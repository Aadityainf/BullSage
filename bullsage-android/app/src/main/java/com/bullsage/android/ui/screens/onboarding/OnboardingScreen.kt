package com.bullsage.android.ui.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bullsage.android.R
import com.bullsage.android.ui.components.previews.ComponentPreview
import com.bullsage.android.ui.components.previews.DayNightPreviews

@Composable
fun OnboardingRoute(
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit
) {
    OnboardingScreen(
        onSignUpClick = onSignUpClick,
        onSignInClick = onSignInClick
    )
}

@Composable
fun OnboardingScreen(
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(18.dp))
        Button(
            onClick = onSignUpClick,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .height(48.dp)
                .width(250.dp)
        ) {
            Text(text = stringResource(id = R.string.create_account))
        }
        Spacer(Modifier.height(10.dp))
        Button(
            onClick = onSignInClick,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .height(48.dp)
                .width(250.dp)
        ) {
            Text(text = stringResource(id = R.string.sign_in))
        }

    }
}

@DayNightPreviews
@Composable
private fun OnboardingScreenPreview() {
    ComponentPreview {
        OnboardingScreen(
            onSignUpClick = {},
            onSignInClick = {}
        )
    }
}