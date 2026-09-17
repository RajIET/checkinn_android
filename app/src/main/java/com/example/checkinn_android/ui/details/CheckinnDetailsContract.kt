package com.example.checkinn_android.ui.details

import android.graphics.Bitmap
import com.example.checkinn_android.domain.model.BookingOrder
import com.example.checkinn_android.domain.model.BookingStatusType
import com.example.checkinn_android.ui.designsystem.components.AppAlert

data class CheckinnDetailsUiState(
    val booking: BookingOrder,
    val roomNumber: String = "",
    val numberOfGuests: Int = 2,
    val checkoutDate: String = "",
    val status: BookingStatusType = BookingStatusType.Initiated,
    val idProofImage: Bitmap? = null,
    val isImageLoading: Boolean = false,
    val isProcessing: Boolean = false,
    val alert: AppAlert? = null,
    val didMakeChanges: Boolean = false
) {
    val showActionButtons: Boolean
        get() = status != BookingStatusType.Approved && status != BookingStatusType.Rejected

    val guestName: String
        get() = booking.customer?.customerName ?: "Guest"

    val phoneNumber: String
        get() = booking.customer?.customerMobile ?: "-"

    val email: String
        get() = booking.customer?.customerEmail ?: "-"

    val address: String
        get() = booking.customer?.customerAddress ?: "-"

    val city: String
        get() = booking.customer?.customerCity ?: "-"

    val residentStatus: String
        get() = booking.customer?.customerResident ?: "-"

    val orderNo: String
        get() = booking.orderNo

    val externalBookingId: String?
        get() = booking.externalBookingId

    val bookingSource: String
        get() = booking.bookingSource ?: "Direct"

    val checkinDateFormatted: String
        get() = booking.checkinDate?.take(10) ?: "-"

    val checkinTimeAgo: String
        get() = booking.createdOn?.take(10)?.let { "$it ago" } ?: "-"

    val roomType: String
        get() = booking.roomType ?: "Standard Room"

    val roomFloor: String
        get() = booking.roomFloor?.let { "Floor $it" } ?: "-"

    val specialRequest: String?
        get() = booking.specialRequest

    val idProofNo: String?
        get() = booking.idProofNo

    val idProofTypeName: String
        get() = booking.idProofTypeName
}

sealed interface CheckinnDetailsUiEvent {
    data class RoomNumberChanged(val roomNumber: String) : CheckinnDetailsUiEvent
    data object IncrementGuests : CheckinnDetailsUiEvent
    data object DecrementGuests : CheckinnDetailsUiEvent
    data class CheckoutDateChanged(val checkoutDate: String) : CheckinnDetailsUiEvent
    data object ApproveCheckin : CheckinnDetailsUiEvent
    data object DenyCheckin : CheckinnDetailsUiEvent
    data object DismissAlert : CheckinnDetailsUiEvent
}

sealed interface CheckinnDetailsUiEffect {
    data object NavigateBack : CheckinnDetailsUiEffect
}
