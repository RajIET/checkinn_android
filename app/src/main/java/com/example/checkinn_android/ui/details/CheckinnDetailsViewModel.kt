package com.example.checkinn_android.ui.details

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkinn_android.core.common.Resource
import com.example.checkinn_android.domain.model.BookingOrder
import com.example.checkinn_android.domain.model.BookingStatusType
import com.example.checkinn_android.domain.usecase.CheckPermissionUseCase
import com.example.checkinn_android.domain.usecase.DownloadIdProofUseCase
import com.example.checkinn_android.domain.usecase.UpdateBookingStatusUseCase
import com.example.checkinn_android.ui.designsystem.components.AppAlert
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
class CheckinnDetailsViewModel @Inject constructor(
    private val updateBookingStatusUseCase: UpdateBookingStatusUseCase,
    private val downloadIdProofUseCase: DownloadIdProofUseCase,
    private val checkPermissionUseCase: CheckPermissionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<CheckinnDetailsUiState?>(null)
    val uiState: StateFlow<CheckinnDetailsUiState?> = _uiState.asStateFlow()

    private val _effect = Channel<CheckinnDetailsUiEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun initBooking(booking: BookingOrder) {
        if (_uiState.value == null) {
            _uiState.value = CheckinnDetailsUiState(
                booking = booking,
                roomNumber = booking.roomNo ?: "",
                numberOfGuests = (booking.noOfGuest?.takeIf { it > 0 }) ?: 1,
                checkoutDate = booking.checkoutDate?.take(10) ?: "",
                status = booking.status
            )
            val isApprovedOrDenied = booking.status == BookingStatusType.Approved ||
                    booking.status == BookingStatusType.Rejected
            val imageCount = booking.idProofImageCount ?: 1
            val hasImageToLoad = imageCount > 0
            if (!isApprovedOrDenied && hasImageToLoad) {
                loadIdProofImages(booking.id, imageCount)
            }
        }
    }

    fun onEvent(event: CheckinnDetailsUiEvent) {
        val currentState = _uiState.value ?: return
        when (event) {
            is CheckinnDetailsUiEvent.RoomNumberChanged -> {
                _uiState.update { it?.copy(roomNumber = event.roomNumber, roomNumberError = null) }
            }
            is CheckinnDetailsUiEvent.IncrementGuests -> {
                if (currentState.numberOfGuests < 10) {
                    _uiState.update { it?.copy(numberOfGuests = it.numberOfGuests + 1, numberOfGuestsError = null) }
                }
            }
            is CheckinnDetailsUiEvent.DecrementGuests -> {
                if (currentState.numberOfGuests > 1) {
                    _uiState.update { it?.copy(numberOfGuests = it.numberOfGuests - 1, numberOfGuestsError = null) }
                }
            }
            is CheckinnDetailsUiEvent.CheckoutDateChanged -> {
                _uiState.update { it?.copy(checkoutDate = event.checkoutDate) }
            }
            is CheckinnDetailsUiEvent.ApproveCheckin -> approveCheckin()
            is CheckinnDetailsUiEvent.DenyCheckin -> denyCheckin()
            is CheckinnDetailsUiEvent.CheckoutCheckin -> checkoutCheckin()
            is CheckinnDetailsUiEvent.DismissAlert -> {
                val shouldDismissScreen = currentState.didMakeChanges
                _uiState.update { it?.copy(alert = null) }
                if (shouldDismissScreen) {
                    viewModelScope.launch { _effect.send(CheckinnDetailsUiEffect.NavigateBack) }
                }
            }
            is CheckinnDetailsUiEvent.FlipIdProofImage -> {
                if (currentState.idProofImages.size == 2) {
                    _uiState.update { it?.copy(activeImageIndex = if (it.activeImageIndex == 0) 1 else 0) }
                }
            }
        }
    }

    private fun loadIdProofImages(bookingId: Int, imageCount: Int) {
        _uiState.update { it?.copy(isImageLoading = true) }
        val bitmaps = MutableList<Bitmap?>(imageCount) { null }
        val pendingCount = java.util.concurrent.atomic.AtomicInteger(imageCount)

        for (i in 0 until imageCount) {
            val imageId = i + 1
            viewModelScope.launch {
                downloadIdProofUseCase(bookingId, imageId).collect { result ->
                    if (result is Resource.Success) {
                        val bytes = result.data
                        val bitmap = if (bytes != null && bytes.isNotEmpty()) {
                            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        } else null
                        bitmaps[i] = bitmap
                    }
                    
                    if (result !is Resource.Loading && pendingCount.decrementAndGet() == 0) {
                        _uiState.update { 
                            it?.copy(
                                isImageLoading = false,
                                idProofImages = bitmaps.filterNotNull()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun approveCheckin() {
        val currentState = _uiState.value ?: return

        if (currentState.roomNumber.isBlank()) {
            _uiState.update {
                it?.copy(
                    roomNumberError = "Room number is required",
                    alert = AppAlert(
                        title = "Room Number Required",
                        message = "Please enter a room number before approving check-in.",
                        buttonTitle = "OK"
                    )
                )
            }
            return
        }

        if (currentState.numberOfGuests <= 0) {
            _uiState.update {
                it?.copy(
                    numberOfGuestsError = "Number of guests is required",
                    alert = AppAlert(
                        title = "Guest Count Required",
                        message = "Please select at least 1 guest before approving check-in.",
                        buttonTitle = "OK"
                    )
                )
            }
            return
        }

        viewModelScope.launch {
            if (!checkPermissionUseCase.hasApprovePermission()) {
                _uiState.update {
                    it?.copy(
                        alert = AppAlert(
                            title = "Not Authorised",
                            message = "You are not authorised to approve",
                            buttonTitle = "OK"
                        )
                    )
                }
                return@launch
            }

            _uiState.update { it?.copy(isProcessing = true, roomNumberError = null, numberOfGuestsError = null) }
            updateBookingStatusUseCase(
                bookingId = currentState.booking.id,
                statusId = 3, // Approved
                noOfGuest = currentState.numberOfGuests,
                roomNumber = currentState.roomNumber.trim(),
                checkoutDate = currentState.checkoutDate.ifBlank { null }
            ).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it?.copy(isProcessing = true) }
                    }
                    is Resource.Success -> {
                        val roomText = if (currentState.roomNumber.isBlank()) "assigned room" else "Room ${currentState.roomNumber}"
                        val guestText = if (currentState.numberOfGuests > 1) "${currentState.numberOfGuests} guests" else "1 guest"
                        _uiState.update {
                            it?.copy(
                                isProcessing = false,
                                status = BookingStatusType.Approved,
                                didMakeChanges = true,
                                alert = AppAlert(
                                    title = "Check-in Approved",
                                    message = "Check-in for ${currentState.guestName} in $roomText for $guestText has been approved.",
                                    buttonTitle = "Done"
                                )
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it?.copy(
                                isProcessing = false,
                                alert = AppAlert(
                                    title = "Failed to Approve",
                                    message = result.message ?: "Could not approve check-in",
                                    buttonTitle = "OK"
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun denyCheckin() {
        val currentState = _uiState.value ?: return
        viewModelScope.launch {
            if (!checkPermissionUseCase.hasDenyPermission()) {
                _uiState.update {
                    it?.copy(
                        alert = AppAlert(
                            title = "Not Authorised",
                            message = "You are not authorised to deny",
                            buttonTitle = "OK"
                        )
                    )
                }
                return@launch
            }

            _uiState.update { it?.copy(isProcessing = true) }
            updateBookingStatusUseCase(
                bookingId = currentState.booking.id,
                statusId = 2, // Declined
                noOfGuest = currentState.numberOfGuests
            ).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it?.copy(isProcessing = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it?.copy(
                                isProcessing = false,
                                status = BookingStatusType.Rejected,
                                didMakeChanges = true,
                                alert = AppAlert(
                                    title = "Check-in Denied",
                                    message = "Check-in request for ${currentState.guestName} (Order: ${currentState.orderNo}) has been denied.",
                                    buttonTitle = "Dismiss"
                                )
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it?.copy(
                                isProcessing = false,
                                alert = AppAlert(
                                    title = "Failed to Deny",
                                    message = result.message ?: "Could not deny check-in",
                                    buttonTitle = "OK"
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun checkoutCheckin() {
        // Checkout API is not yet available — UI-only placeholder.
        val currentState = _uiState.value ?: return
        _uiState.update {
            it?.copy(
                alert = AppAlert(
                    title = "Checkout",
                    message = "Checkout for ${currentState.guestName} (Order: ${currentState.orderNo}) is not yet supported.",
                    buttonTitle = "OK"
                )
            )
        }
    }
}
