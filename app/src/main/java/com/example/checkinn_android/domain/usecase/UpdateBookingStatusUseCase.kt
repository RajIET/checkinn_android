package com.example.checkinn_android.domain.usecase

import com.example.checkinn_android.core.common.Resource
import com.example.checkinn_android.domain.repository.BookingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateBookingStatusUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(
        bookingId: Int,
        statusId: Int,
        noOfGuest: Int?,
        roomNumber: String? = null,
        checkoutDate: String? = null
    ): Flow<Resource<Unit>> {
        return repository.updateBookingStatus(bookingId, statusId, noOfGuest, roomNumber, checkoutDate)
    }
}
