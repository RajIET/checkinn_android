package com.example.checkinn_android.domain.repository

import com.example.checkinn_android.core.common.Resource
import com.example.checkinn_android.domain.model.BookingOrder
import kotlinx.coroutines.flow.Flow

interface BookingRepository {
    fun getBookingOrders(): Flow<Resource<List<BookingOrder>>>
    suspend fun updateBookingStatus(bookingId: Int, statusId: Int, noOfGuest: Int?): Flow<Resource<Unit>>
    suspend fun downloadIdProof(id: Int): Flow<Resource<ByteArray>>
}
