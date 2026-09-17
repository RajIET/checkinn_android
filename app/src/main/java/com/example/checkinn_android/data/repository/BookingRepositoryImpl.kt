package com.example.checkinn_android.data.repository

import com.example.checkinn_android.core.common.DispatcherProvider
import com.example.checkinn_android.core.common.Resource
import com.example.checkinn_android.data.remote.api.AuthApiService
import com.example.checkinn_android.data.remote.dto.BookingOrderDto
import com.example.checkinn_android.data.remote.dto.UpdateBookingStatusRequestDto
import com.example.checkinn_android.domain.model.BookingOrder
import com.example.checkinn_android.domain.model.BookingStatus
import com.example.checkinn_android.domain.model.BookingStatusType
import com.example.checkinn_android.domain.model.Customer
import com.example.checkinn_android.domain.repository.BookingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Token attachment and 401-based refresh are now handled transparently by
 * [com.example.checkinn_android.data.remote.interceptor.AuthInterceptor] and
 * [com.example.checkinn_android.data.remote.interceptor.TokenAuthenticator].
 *
 * No manual token reads or [retrofit2.http.Header] parameters needed here.
 */
class BookingRepositoryImpl @Inject constructor(
    private val apiService: AuthApiService,
    private val dispatchers: DispatcherProvider
) : BookingRepository {

    override fun getBookingOrders(): Flow<Resource<List<BookingOrder>>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.getBookingOrders()
            if (response.isSuccessful) {
                val dtos = response.body()?.data?.bookingOrders ?: emptyList()
                val orders = dtos.map { it.toDomain() }
                    .sortedWith(
                        compareBy<BookingOrder> { it.status.sortPriority }
                            .thenByDescending { it.checkinDate ?: "" }
                    )
                emit(Resource.Success(orders))
            } else {
                val errorMsg = response.errorBody()?.string()?.ifBlank { null }
                    ?: "Failed to fetch check-ins (${response.code()})"
                emit(Resource.Error(errorMsg))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Network error occurred"))
        }
    }.flowOn(dispatchers.io)

    override suspend fun updateBookingStatus(
        bookingId: Int,
        statusId: Int,
        noOfGuest: Int?
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            val request = UpdateBookingStatusRequestDto(
                bookingId = bookingId,
                statusId = statusId,
                noOfGuest = noOfGuest
            )
            val response = apiService.updateBookingStatus(request)
            if (response.isSuccessful) {
                emit(Resource.Success(Unit))
            } else {
                val errorMsg = response.errorBody()?.string()?.ifBlank { null }
                    ?: "Failed to update booking status (${response.code()})"
                emit(Resource.Error(errorMsg))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Failed to update check-in status"))
        }
    }.flowOn(dispatchers.io)

    override suspend fun downloadIdProof(id: Int): Flow<Resource<ByteArray>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiService.downloadIdProof(id)
            if (response.isSuccessful && response.body() != null) {
                emit(Resource.Success(response.body()!!.bytes()))
            } else {
                emit(Resource.Error("Could not download ID proof image"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Failed to download ID proof"))
        }
    }.flowOn(dispatchers.io)

    private fun BookingOrderDto.toDomain(): BookingOrder {
        return BookingOrder(
            id = id,
            orderNo = orderNo ?: id.toString(),
            externalBookingId = externalBookingId,
            bookingSource = bookingSource,
            hotelId = hotelId ?: 0,
            roomNo = roomNo,
            customerId = customerId,
            roomFloor = roomFloor,
            roomType = roomType,
            specialRequest = specialRequest,
            checkinDate = checkinDate,
            modifiedOn = modifiedOn,
            checkoutDate = checkoutDate,
            createdOn = createdOn,
            idProofTypeId = idProofTypeId,
            idProofNo = idProofNo,
            idProofImagePath = idProofImagePath,
            statusId = statusId,
            bookingStatus = bookingStatus?.let {
                BookingStatus(
                    statusId = it.statusId,
                    orderStatus = BookingStatusType.fromRawValue(it.orderStatus)
                )
            },
            customer = customer?.let {
                Customer(
                    customerId = it.customerId,
                    customerMobile = it.customerMobile,
                    customerEmail = it.customerEmail,
                    customerAddress = it.customerAddress,
                    customerIdNumber = it.customerIdNumber,
                    customerCity = it.customerCity,
                    customerResident = it.customerResident,
                    customerName = it.customerName
                )
            }
        )
    }
}
