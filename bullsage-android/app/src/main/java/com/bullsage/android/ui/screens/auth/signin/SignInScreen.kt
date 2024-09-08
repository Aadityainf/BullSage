package com.bullsage.android.ui.screens.auth.signin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bullsage.android.R
import com.bullsage.android.ui.components.BackButton
import com.bullsage.android.ui.components.previews.ComponentPreview
import com.bullsage.android.ui.components.previews.DayNightPreviews
import com.bullsage.android.ui.screens.auth.components.AuthForm

@Composable
fun SignInRoute(
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,
    onBackClick: () -> Unit
) {
    SignInScreen(
        onSignUpClick = onSignUpClick,
        onSignInClick = onSignInClick,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignInScreen(
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = { BackButton(onClick = onBackClick) }
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(40.dp))
            Text(
                text = stringResource(id = R.string.sign_in_title),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(10.dp))
            NoAccount(
                onSignUpClick = onSignUpClick
            )
            Spacer(Modifier.height(40.dp))
            AuthForm(
                email = "",
                onEmailChange = {},
                password = "",
                onPasswordChange = {},
                passwordVisible = false,
                onPasswordVisibilityChange = {}
            )
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = onSignInClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 10.dp)
            ) {
                Text(text = stringResource(id = R.string.sign_in))
            }
        }
    }
}

@Composable
private fun NoAccount(
    onSignUpClick: () -> Unit
) {
    Row {
        Text(
            text = stringResource(id = R.string.no_account)
        )
        Text(
            text = stringResource(id = R.string.sign_up),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { onSignUpClick() }
        )
    }
}

@DayNightPreviews
@Composable
private fun SignInScreenPreview() {
    ComponentPreview {
        SignInScreen(
            onSignUpClick = {},
            onSignInClick = {},
            onBackClick = {}
        )
    }
}