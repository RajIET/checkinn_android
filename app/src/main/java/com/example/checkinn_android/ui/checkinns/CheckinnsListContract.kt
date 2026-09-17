package com.example.checkinn_android.ui.checkinns

import com.example.checkinn_android.domain.model.BookingOrder

data class CheckinnsListUiState(
    val orders: List<BookingOrder> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed interface CheckinnsListUiEvent {
    data object Refresh : CheckinnsListUiEvent
}

sealed interface CheckinnsListUiEffect {
    data class NavigateToDetails(val booking: BookingOrder) : CheckinnsListUiEffect
}
