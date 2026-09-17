package com.example.checkinn_android.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkinn_android.domain.usecase.GetLoggedInUserUseCase
import com.example.checkinn_android.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _effect = Channel<HomeUiEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        observeUser()
    }

    private fun observeUser() {
        getLoggedInUserUseCase().onEach { user ->
            _uiState.update {
                it.copy(
                    user = user,
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.LogoutClicked -> logout()
        }
    }

    private fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _effect.send(HomeUiEffect.NavigateToLogin)
        }
    }
}
