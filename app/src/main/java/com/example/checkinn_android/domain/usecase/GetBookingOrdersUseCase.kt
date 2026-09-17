package com.example.checkinn_android.domain.usecase

import com.example.checkinn_android.core.common.Resource
import com.example.checkinn_android.domain.model.BookingOrder
import com.example.checkinn_android.domain.repository.BookingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookingOrdersUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    operator fun invoke(): Flow<Resource<List<BookingOrder>>> {
        return repository.getBookingOrders()
    }
}
