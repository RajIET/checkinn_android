package com.example.checkinn_android.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ValidateCredentialsUseCaseTest {

    private lateinit var validateCredentialsUseCase: ValidateCredentialsUseCase

    @Before
    fun setUp() {
        validateCredentialsUseCase = ValidateCredentialsUseCase()
    }

    // --- Username validation ---

    @Test
    fun `empty username returns error`() {
        val result = validateCredentialsUseCase("", "anypassword")
        assertFalse(result.isValid)
        assertEquals("Enter your username.", result.usernameError)
    }

    @Test
    fun `whitespace only username returns error`() {
        val result = validateCredentialsUseCase("   ", "anypassword")
        assertFalse(result.isValid)
        assertEquals("Enter your username.", result.usernameError)
    }

    @Test
    fun `valid plain username passes`() {
        val result = validateCredentialsUseCase("frontdesk", "pass")
        assertTrue(result.isValid)
        assertNull(result.usernameError)
    }

    @Test
    fun `email as username passes`() {
        val result = validateCredentialsUseCase("manager@checkinn.app", "pass")
        assertTrue(result.isValid)
        assertNull(result.usernameError)
    }

    @Test
    fun `mobile as username passes`() {
        val result = validateCredentialsUseCase("9876543210", "pass")
        assertTrue(result.isValid)
        assertNull(result.usernameError)
    }

    // --- Password validation removed ---

    @Test
    fun `empty password passes (no password validation)`() {
        val result = validateCredentialsUseCase("frontdesk", "")
        assertTrue(result.isValid)
        assertNull(result.passwordError)
    }

    @Test
    fun `short password passes (no minimum length requirement)`() {
        val result = validateCredentialsUseCase("frontdesk", "123")
        assertTrue(result.isValid)
        assertNull(result.passwordError)
    }

    // --- Combined ---

    @Test
    fun `both fields empty only returns username error`() {
        val result = validateCredentialsUseCase("", "")
        assertFalse(result.isValid)
        assertEquals("Enter your username.", result.usernameError)
        assertNull(result.passwordError)
    }
}
