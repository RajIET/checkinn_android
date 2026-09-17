package com.example.checkinn_android.ui.login

import com.example.checkinn_android.core.common.Resource
import com.example.checkinn_android.domain.model.User
import com.example.checkinn_android.domain.repository.AuthRepository
import com.example.checkinn_android.domain.usecase.LoginUseCase
import com.example.checkinn_android.domain.usecase.ValidateCredentialsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

private class FakeAuthRepository : AuthRepository {
    override fun login(username: String, password: String): Flow<Resource<User>> {
        return flowOf(
            Resource.Success(
                User(
                    id = 1,
                    firstName = "Front",
                    lastName = "Desk",
                    username = username
                )
            )
        )
    }

    override fun getLoggedInUser(): Flow<User?> = flowOf(null)

    override suspend fun logout() {}
}

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        val authRepository = FakeAuthRepository()
        val loginUseCase = LoginUseCase(authRepository)
        val validateCredentialsUseCase = ValidateCredentialsUseCase()
        loginViewModel = LoginViewModel(loginUseCase, validateCredentialsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialStateIsEmpty() {
        val state = loginViewModel.uiState.value
        assertEquals("", state.username)
        assertEquals("", state.password)
        assertNull(state.usernameError)
        assertNull(state.passwordError)
        assertFalse(state.isLoading)
        assertTrue(state.isLoginEnabled)
    }

    @Test
    fun testValidationFailsWhenUsernameEmpty() {
        loginViewModel.onEvent(LoginUiEvent.UsernameChanged(""))
        loginViewModel.onEvent(LoginUiEvent.PasswordChanged("anypass"))

        val isValid = loginViewModel.validate()

        assertFalse(isValid)
        assertEquals("Enter your username.", loginViewModel.uiState.value.usernameError)
        assertNull(loginViewModel.uiState.value.passwordError)
    }

    @Test
    fun testValidationPassesWithAnyUsername() {
        loginViewModel.onEvent(LoginUiEvent.UsernameChanged("frontdesk"))
        loginViewModel.onEvent(LoginUiEvent.PasswordChanged("anypass"))

        assertTrue(loginViewModel.validate())
        assertNull(loginViewModel.uiState.value.usernameError)
        assertNull(loginViewModel.uiState.value.passwordError)
    }

    @Test
    fun testValidationPassesWithEmailAsUsername() {
        loginViewModel.onEvent(LoginUiEvent.UsernameChanged("manager@checkinn.app"))
        loginViewModel.onEvent(LoginUiEvent.PasswordChanged("secure123"))

        assertTrue(loginViewModel.validate())
        assertNull(loginViewModel.uiState.value.usernameError)
    }

    @Test
    fun testValidationPassesWithMobileAsUsername() {
        loginViewModel.onEvent(LoginUiEvent.UsernameChanged("9876543210"))
        loginViewModel.onEvent(LoginUiEvent.PasswordChanged("mypassword"))

        assertTrue(loginViewModel.validate())
        assertNull(loginViewModel.uiState.value.usernameError)
    }

    @Test
    fun testValidationPassesWithEmptyPassword() {
        loginViewModel.onEvent(LoginUiEvent.UsernameChanged("frontdesk"))
        loginViewModel.onEvent(LoginUiEvent.PasswordChanged(""))

        assertTrue(loginViewModel.validate())
        assertNull(loginViewModel.uiState.value.passwordError)
    }

    @Test
    fun testEditingUsernameClearsError() {
        loginViewModel.validate() // triggers empty username error
        loginViewModel.onEvent(LoginUiEvent.UsernameChanged("frontdesk"))
        assertNull(loginViewModel.uiState.value.usernameError)
    }

    @Test
    fun testClearValidationMessagesResetsErrors() {
        loginViewModel.validate()
        loginViewModel.onEvent(LoginUiEvent.ClearValidationMessages)
        assertNull(loginViewModel.uiState.value.usernameError)
        assertNull(loginViewModel.uiState.value.passwordError)
    }

    @Test
    fun testIsLoginEnabledWhenNotLoading() {
        assertTrue(loginViewModel.uiState.value.isLoginEnabled)
    }
}
