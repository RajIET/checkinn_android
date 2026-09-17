package com.example.checkinn_android.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkinn_android.core.common.Resource
import com.example.checkinn_android.core.util.normalizedLoginCredential
import com.example.checkinn_android.domain.usecase.LoginUseCase
import com.example.checkinn_android.domain.usecase.ValidateCredentialsUseCase
import com.example.checkinn_android.ui.designsystem.components.AppAlert
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val validateCredentialsUseCase: ValidateCredentialsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _effect = Channel<LoginUiEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.UsernameChanged -> {
                _uiState.update { it.copy(username = event.username, usernameError = null) }
            }
            is LoginUiEvent.EmailChanged -> {
                _uiState.update { it.copy(username = event.email, usernameError = null) }
            }
            is LoginUiEvent.PasswordChanged -> {
                _uiState.update { it.copy(password = event.password, passwordError = null) }
            }
            is LoginUiEvent.TogglePasswordVisibility -> {
                _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }
            is LoginUiEvent.ClearValidationMessages -> {
                _uiState.update { it.copy(usernameError = null, passwordError = null) }
            }
            is LoginUiEvent.DismissError -> {
                _uiState.update { it.copy(errorMessage = null) }
            }
            is LoginUiEvent.DismissAlert -> {
                _uiState.update { it.copy(alert = null) }
            }
            is LoginUiEvent.SubmitLogin -> {
                submitLogin()
            }
        }
    }

    /**
     * Executes input validation matching iOS LoginViewModel.validate().
     * Clears prior errors, validates inputs, and sets inline error messages if invalid.
     */
    fun validate(): Boolean {
        val currentState = _uiState.value
        val validation = validateCredentialsUseCase(currentState.username, currentState.password)

        _uiState.update {
            it.copy(
                usernameError = validation.usernameError,
                passwordError = validation.passwordError
            )
        }

        return validation.isValid
    }

    private fun submitLogin() {
        if (!validate()) {
            return
        }

        val currentState = _uiState.value
        val username = currentState.username.trim()

        viewModelScope.launch {
            loginUseCase(username, currentState.password).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true, errorMessage = null, alert = null) }
                    }
                    is Resource.Success -> {
                        _uiState.update { it.copy(isLoading = false, errorMessage = null, alert = null) }
                        _effect.send(LoginUiEffect.NavigateToHome)
                    }
                    is Resource.Error -> {
                        val alertMessage = "Your username or password is incorrect. Please try again."
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.message,
                                alert = AppAlert(
                                    title = "Login Failed",
                                    message = alertMessage
                                )
                            )
                        }
                        _effect.send(LoginUiEffect.ShowSnackbar(result.message))
                    }
                }
            }
        }
    }
}
