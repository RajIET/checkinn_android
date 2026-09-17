package com.example.checkinn_android.ui.login

import com.example.checkinn_android.ui.designsystem.components.AppAlert

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val usernameError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val alert: AppAlert? = null
) {
    /**
     * Matches iOS LoginViewModel.isLoginEnabled: !isLoading.
     * The submit button remains interactive until an async login request is in flight.
     */
    val isLoginEnabled: Boolean
        get() = !isLoading

    // Backward compatibility aliases
    val email: String
        get() = username

    val emailError: String?
        get() = usernameError
}

sealed interface LoginUiEvent {
    data class UsernameChanged(val username: String) : LoginUiEvent
    data class EmailChanged(val email: String) : LoginUiEvent
    data class PasswordChanged(val password: String) : LoginUiEvent
    data object TogglePasswordVisibility : LoginUiEvent
    data object ClearValidationMessages : LoginUiEvent
    data object SubmitLogin : LoginUiEvent
    data object DismissError : LoginUiEvent
    data object DismissAlert : LoginUiEvent
}

sealed interface LoginUiEffect {
    data object NavigateToHome : LoginUiEffect
    data class ShowSnackbar(val message: String) : LoginUiEffect
}
