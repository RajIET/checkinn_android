package com.example.checkinn_android.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.checkinn_android.ui.designsystem.DS
import com.example.checkinn_android.ui.designsystem.DSColors
import com.example.checkinn_android.ui.designsystem.DSTypography
import com.example.checkinn_android.ui.designsystem.components.DSAlertDialog
import com.example.checkinn_android.ui.designsystem.components.DSButton
import com.example.checkinn_android.ui.designsystem.components.DSButtonVariant
import com.example.checkinn_android.ui.designsystem.components.DSCard
import com.example.checkinn_android.ui.designsystem.components.DSContentHeader
import com.example.checkinn_android.ui.designsystem.components.DSContentHeaderStyle
import com.example.checkinn_android.ui.designsystem.components.DSTextField
import com.example.checkinn_android.ui.designsystem.components.DSUsernameField
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is LoginUiEffect.NavigateToHome -> onNavigateToHome()
                is LoginUiEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    LoginContent(
        state = state,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(
    state: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Sign in",
                        style = DSTypography.title,
                        color = DSColors.primaryText
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DSColors.background
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = DSColors.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DSColors.background),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = DS.Layout.readableMaxWidth)
                    .verticalScroll(rememberScrollState())
                    .padding(DS.Spacing.md)
            ) {
                // Welcome Header matching iOS LoginWellcomeView
                DSContentHeader(
                    title = "🙋‍♂️🙋‍♀️ Welcome to CheckInn",
                    subtitle = "Use your registered email or mobile number to get CheckInn access",
                    style = DSContentHeaderStyle.Hero
                )

                Spacer(modifier = Modifier.height(DS.Spacing.xl))

                // Card Container matching iOS DSCard
                DSCard {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Instruction Header matching iOS LoginInstructionView
                        DSContentHeader(
                            title = "Account details",
                            subtitle = "Please log in with your username and password to access your CheckInns.",
                            style = DSContentHeaderStyle.Section
                        )

                        Spacer(modifier = Modifier.height(DS.Spacing.lg))

                        // Username Field matching iOS DSUsernameField
                        DSUsernameField(
                            value = state.username,
                            onValueChange = { onEvent(LoginUiEvent.UsernameChanged(it)) },
                            placeholder = "Enter your username",
                            errorMessage = state.usernameError,
                            imeAction = ImeAction.Next,
                            modifier = Modifier.testTag("login-username-field")
                        )

                        Spacer(modifier = Modifier.height(DS.Spacing.md))

                        // Password Field matching iOS DSTextField(isSecure: true)
                        DSTextField(
                            title = "Password",
                            placeholder = "Enter password",
                            leadingIcon = Icons.Default.Lock,
                            value = state.password,
                            onValueChange = { onEvent(LoginUiEvent.PasswordChanged(it)) },
                            isSecure = true,
                            errorMessage = state.passwordError,
                            modifier = Modifier.testTag("login-password-field"),
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                keyboardType = androidx.compose.ui.text.input.KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                                onDone = { onEvent(LoginUiEvent.SubmitLogin) }
                            )
                        )

                        state.errorMessage?.let { error ->
                            Spacer(modifier = Modifier.height(DS.Spacing.sm))
                            Text(
                                text = error,
                                color = DSColors.danger,
                                style = DSTypography.footnote,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(DS.Spacing.lg))

                        // DSButton matching iOS DSButton
                        DSButton(
                            title = if (state.isLoading) "Signing in" else "Sign in",
                            icon = Icons.AutoMirrored.Filled.ArrowForward,
                            variant = DSButtonVariant.Primary,
                            isLoading = state.isLoading,
                            isDisabled = !state.isLoginEnabled,
                            modifier = Modifier.testTag("login-submit-button"),
                            action = { onEvent(LoginUiEvent.SubmitLogin) }
                        )
                    }
                }
            }

            DSAlertDialog(
                alert = state.alert,
                onDismissRequest = { onEvent(LoginUiEvent.DismissAlert) }
            )
        }
    }
}
