package com.example.checkinn_android.ui.profile

import com.example.checkinn_android.domain.model.User

data class ProfileUiState(
    val user: User? = null,
    val isLoading: Boolean = false
)

sealed interface ProfileUiEvent {
    data object SignOut : ProfileUiEvent
}

sealed interface ProfileUiEffect {
    data object NavigateToLogin : ProfileUiEffect
}
