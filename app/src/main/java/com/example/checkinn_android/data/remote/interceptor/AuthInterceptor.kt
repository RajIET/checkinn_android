package com.example.checkinn_android.data.remote.interceptor

import android.util.Log
import com.example.checkinn_android.data.local.security.SecureTokenStorage
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * OkHttp [Interceptor] that attaches the stored access token as a Bearer
 * Authorization header to every outgoing request.
 *
 * Auth endpoints (login, refresh) are intentionally skipped so they never
 * receive a stale/invalid token header that could confuse the server.
 *
 * This interceptor is **not** responsible for refreshing tokens — that is
 * handled by [TokenAuthenticator].
 */
@Singleton
class AuthInterceptor @Inject constructor(
    private val secureTokenStorage: SecureTokenStorage
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Skip token injection for auth endpoints to avoid double-headers
        val path = originalRequest.url.encodedPath
        if (path.contains("Auth/login", ignoreCase = true) ||
            path.contains("Auth/refresh", ignoreCase = true)
        ) {
            Log.d("API_DEBUGGER", "[AuthInterceptor] Skipping token header for auth endpoint: $path")
            return chain.proceed(originalRequest)
        }

        val accessToken = secureTokenStorage.getAccessToken()

        val request = if (!accessToken.isNullOrBlank()) {
            Log.d("API_DEBUGGER", "[AuthInterceptor] Attaching Bearer token to request: $path")
            originalRequest.newBuilder()
                .removeHeader("Authorization")
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
        } else {
            Log.w("API_DEBUGGER", "[AuthInterceptor] No access token found in storage for request: $path")
            originalRequest
        }

        return chain.proceed(request)
    }
}
