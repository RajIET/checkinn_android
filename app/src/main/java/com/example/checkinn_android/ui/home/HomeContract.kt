package com.example.checkinn_android.ui.home

import com.example.checkinn_android.domain.model.User

data class HomeUiState(
    val user: User? = null,
    val isLoading: Boolean = true
)

sealed interface HomeUiEvent {
    data object LogoutClicked : HomeUiEvent
}

sealed interface HomeUiEffect {
    data object NavigateToLogin : HomeUiEffect
}
