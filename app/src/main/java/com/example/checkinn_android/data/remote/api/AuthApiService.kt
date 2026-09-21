package com.example.checkinn_android.data.remote.api

import com.example.checkinn_android.data.remote.dto.BookingResponseDto
import com.example.checkinn_android.data.remote.dto.LoginRequest
import com.example.checkinn_android.data.remote.dto.LoginResponse
import com.example.checkinn_android.data.remote.dto.UpdateBookingStatusRequestDto
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Main Retrofit service backed by the primary [OkHttpClient] that includes
 * [com.example.checkinn_android.data.remote.interceptor.AuthInterceptor] and
 * [com.example.checkinn_android.data.remote.interceptor.TokenAuthenticator].
 *
 * Token attachment and 401 handling are transparent — no [retrofit2.http.Header]
 * parameters required on protected endpoints.
 */
interface AuthApiService {

    @POST("Auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @GET("BookingOrders/GetBookingOrders")
    suspend fun getBookingOrders(): Response<BookingResponseDto>

    @PUT("BookingOrders/UpdateBookingStatus")
    suspend fun updateBookingStatus(
        @Body request: UpdateBookingStatusRequestDto
    ): Response<ResponseBody>

    @GET("BookingOrders/{id}/IdProof")
    suspend fun downloadIdProof(
        @Path("id") id: Int,
        @Query("imageId") imageId: Int
    ): Response<ResponseBody>
}
