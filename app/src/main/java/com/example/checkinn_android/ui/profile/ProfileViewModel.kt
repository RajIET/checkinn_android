package com.example.checkinn_android.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkinn_android.domain.usecase.GetLoggedInUserUseCase
import com.example.checkinn_android.domain.usecase.LogoutUseCase
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
class ProfileViewModel @Inject constructor(
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _effect = Channel<ProfileUiEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        observeUser()
    }

    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.SignOut -> signOut()
        }
    }

    private fun observeUser() {
        viewModelScope.launch {
            getLoggedInUserUseCase().collect { user ->
                _uiState.update { it.copy(user = user) }
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            logoutUseCase()
            _effect.send(ProfileUiEffect.NavigateToLogin)
        }
    }
}
