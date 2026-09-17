package com.example.checkinn_android.domain.usecase

import javax.inject.Inject

data class ValidationResult(
    val isValid: Boolean,
    val usernameError: String? = null,
    val passwordError: String? = null
) {
    // Backward compatibility alias
    val emailError: String?
        get() = usernameError
}

/**
 * Validates login input.
 * Username field accepts any username (must not be empty).
 * Email and password field validations are removed.
 */
class ValidateCredentialsUseCase @Inject constructor() {
    operator fun invoke(username: String, password: String): ValidationResult {
        val trimmedUsername = username.trim()

        val usernameError = if (trimmedUsername.isEmpty()) {
            "Enter your username."
        } else {
            null
        }

        // Password field validation removed as requested
        val passwordError: String? = null

        return ValidationResult(
            isValid = usernameError == null && passwordError == null,
            usernameError = usernameError,
            passwordError = passwordError
        )
    }
}
