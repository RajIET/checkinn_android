package com.example.checkinn_android.core.util

enum class LoginCredentialKind {
    Email,
    Mobile,
    Unknown
}

/**
 * Classifies login credentials into Email, Mobile, or Unknown.
 * Ported directly from CheckInn iOS String+LoginCredentialValidation.swift.
 */
val String.loginCredentialKind: LoginCredentialKind
    get() {
        val trimmedValue = trim()

        if (trimmedValue.contains("@")) {
            return LoginCredentialKind.Email
        }

        if (trimmedValue.any { it.isDigit() } && trimmedValue.all { it.isDigit() || it.isWhitespace() || it == '-' }) {
            return LoginCredentialKind.Mobile
        }

        return LoginCredentialKind.Unknown
    }

/**
 * Normalizes login credentials:
 * - Mobile: extracts first 10 digits
 * - Email / Other: trims leading and trailing whitespace
 */
val String.normalizedLoginCredential: String
    get() {
        if (loginCredentialKind == LoginCredentialKind.Mobile) {
            return filter { it.isDigit() }.take(10)
        }

        return trim()
    }

/**
 * Validates email addresses matching the iOS rules:
 * - Exactly two parts split by '@'
 * - Non-empty local part
 * - Domain part contains a dot '.'
 * - Domain does not start or end with a dot '.'
 * - No spaces allowed
 */
val String.isValidLoginEmailAddress: Boolean
    get() {
        if (contains(" ")) return false
        val parts = split("@")

        if (parts.size != 2) return false
        val local = parts[0]
        val domain = parts[1]

        if (local.isEmpty() || !domain.contains(".") || domain.startsWith(".") || domain.endsWith(".")) {
            return false
        }

        return true
    }

/**
 * Validates Indian mobile numbers matching the iOS rules:
 * - Exactly 10 digits
 * - Starts with 6, 7, 8, or 9
 */
val String.isValidIndianMobileNumber: Boolean
    get() {
        if (length != 10) return false
        if (!all { it.isDigit() }) return false
        val firstDigit = firstOrNull() ?: return false
        return firstDigit in listOf('6', '7', '8', '9')
    }

object ValidationUtil {
    const val MIN_PASSWORD_LENGTH = 0

    fun detectCredentialKind(input: String): LoginCredentialKind = input.loginCredentialKind

    fun normalizeCredential(input: String): String = input.trim()

    fun isValidEmail(email: String): Boolean = email.isValidLoginEmailAddress

    fun isValidIndianMobileNumber(input: String): Boolean = input.normalizedLoginCredential.isValidIndianMobileNumber

    fun isValidUsername(input: String): Boolean {
        return input.isNotBlank()
    }

    fun isValidPassword(password: String): Boolean {
        return true
    }
}
