package com.example.checkinn_android.ui.details

import android.graphics.Bitmap
import com.example.checkinn_android.domain.model.BookingOrder
import com.example.checkinn_android.domain.model.BookingStatusType
import com.example.checkinn_android.ui.designsystem.components.AppAlert

data class CheckinnDetailsUiState(
    val booking: BookingOrder,
    val roomNumber: String = "",
    val roomNumberError: String? = null,
    val numberOfGuests: Int = 1,
    val numberOfGuestsError: String? = null,
    val checkoutDate: String = "",
    val status: BookingStatusType = BookingStatusType.Initiated,
    val idProofImages: List<Bitmap> = emptyList(),
    val activeImageIndex: Int = 0,
    val isImageLoading: Boolean = false,
    val isProcessing: Boolean = false,
    val alert: AppAlert? = null,
    val didMakeChanges: Boolean = false
) {
    val showActionButtons: Boolean
        get() = status != BookingStatusType.Approved && status != BookingStatusType.Rejected

    val showCheckoutButton: Boolean
        get() = status == BookingStatusType.Approved

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

    /** The bitmap currently shown in the image viewer. */
    val activeBitmap: Bitmap?
        get() = idProofImages.getOrNull(activeImageIndex)

    /** True only when two images are fetched and available to flip between. */
    val showFlipButton: Boolean
        get() = idProofImages.size == 2
}

sealed interface CheckinnDetailsUiEvent {
    data class RoomNumberChanged(val roomNumber: String) : CheckinnDetailsUiEvent
    data object IncrementGuests : CheckinnDetailsUiEvent
    data object DecrementGuests : CheckinnDetailsUiEvent
    data class CheckoutDateChanged(val checkoutDate: String) : CheckinnDetailsUiEvent
    data object ApproveCheckin : CheckinnDetailsUiEvent
    data object DenyCheckin : CheckinnDetailsUiEvent
    data object CheckoutCheckin : CheckinnDetailsUiEvent
    data object DismissAlert : CheckinnDetailsUiEvent
    data object FlipIdProofImage : CheckinnDetailsUiEvent
}

sealed interface CheckinnDetailsUiEffect {
    data object NavigateBack : CheckinnDetailsUiEffect
}
