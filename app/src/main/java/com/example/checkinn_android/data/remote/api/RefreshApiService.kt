package com.example.checkinn_android.data.remote.api

import com.example.checkinn_android.data.remote.dto.RefreshTokenRequest
import com.example.checkinn_android.data.remote.dto.RefreshTokenResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Isolated Retrofit service used **exclusively** by
 * [com.example.checkinn_android.data.remote.interceptor.TokenAuthenticator]
 * for the token refresh call.
 *
 * This service is backed by a **bare [okhttp3.OkHttpClient]** with no
 * [com.example.checkinn_android.data.remote.interceptor.AuthInterceptor] and
 * no [com.example.checkinn_android.data.remote.interceptor.TokenAuthenticator]
 * attached, ensuring the refresh request can never trigger another 401
 * authentication cycle.
 */
interface RefreshApiService {

    /**
     * Refreshes the JWT pair.
     *
     * @param authorization Current (possibly expired) access token in
     *   `Bearer <token>` format, as required by the server contract.
     * @param body Contains the opaque refresh token.
     */
    @POST("Auth/refresh")
    suspend fun refreshToken(
        @Header("Authorization") authorization: String,
        @Body body: RefreshTokenRequest
    ): Response<RefreshTokenResponseDto>
}
