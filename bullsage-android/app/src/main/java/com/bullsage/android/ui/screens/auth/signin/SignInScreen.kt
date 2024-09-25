package com.bullsage.android.ui.screens.auth.signin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bullsage.android.R
import com.bullsage.android.ui.components.BackButton
import com.bullsage.android.ui.components.auth.AuthForm
import com.bullsage.android.ui.components.previews.ComponentPreview
import com.bullsage.android.ui.components.previews.DayNightPreviews
import kotlinx.coroutines.launch

@Composable
fun SignInRoute(
    onSignUpClick: () -> Unit,
    onSignInSuccessful: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SignInScreen(
        uiState = uiState,
        onEmailChange = viewModel::changeEmail,
        onPasswordChange = viewModel::changePassword,
        onPasswordVisibilityChange = viewModel::changePasswordVisibility,
        onErrorShown = viewModel::errorShown,
        onSignInClick = viewModel::signIn,
        onSignInSuccessful = onSignInSuccessful,
        onSignUpClick = onSignUpClick,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignInScreen(
    uiState: SignInUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityChange: () -> Unit,
    onErrorShown: () -> Unit,
    onSignInClick: () -> Unit,
    onSignInSuccessful: () -> Unit,
    onSignUpClick: () -> Unit,
    onBackClick: () -> Unit
) {
    LaunchedEffect(uiState.signInSuccessful) {
        if (uiState.signInSuccessful) {
            onSignInSuccessful()
        }
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    uiState.errorMessage?.let {
        scope.launch { snackbarHostState.showSnackbar(it) }
        onErrorShown()
    }

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
                email = uiState.email,
                password = uiState.password,
                passwordVisible = uiState.passwordVisible,
                authButtonEnabled = uiState.signInButtonEnabled,
                authButtonText = stringResource(id = R.string.sign_in),
                onEmailChange = onEmailChange,
                onPasswordChange = onPasswordChange,
                onPasswordVisibilityChange = onPasswordVisibilityChange,
                onAuthButtonClick = onSignInClick
            )
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
            uiState = SignInUiState(),
            onEmailChange = {},
            onPasswordChange = {},
            onPasswordVisibilityChange = {},
            onErrorShown = {},
            onSignInClick = {},
            onSignInSuccessful = {},
            onSignUpClick = {},
            onBackClick = {}
        )
    }
}