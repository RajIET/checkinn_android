package com.example.checkinn_android.data.remote.interceptor

import android.util.Log
import com.example.checkinn_android.core.session.SessionManager
import com.example.checkinn_android.data.local.dao.UserDao
import com.example.checkinn_android.data.local.security.SecureTokenStorage
import com.example.checkinn_android.data.remote.api.RefreshApiService
import com.example.checkinn_android.data.remote.dto.RefreshTokenRequest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * OkHttp [Authenticator] that handles `401 Unauthorized` responses by
 * transparently refreshing the access token, storing it securely, and retrying the failed request.
 *
 * ### Behavior rules:
 * 1. On any API call returning 401: Mutex-guarded token refresh is triggered.
 * 2. If token refresh succeeds: saves new access + refresh tokens to EncryptedSharedPreferences (KeyStore),
 *    updates Room DB, and retries the original failed request with the new access token header.
 * 3. If token refresh API explicitly returns 401 / 400 / 403 (invalid or expired refresh token):
 *    clears stored tokens from KeyStore and routes the user to the Login screen.
 * 4. On transient network failures during refresh (e.g. lost internet connection), stored tokens are preserved
 *    so the user is not unexpectedly logged out.
 */
@Singleton
class TokenAuthenticator @Inject constructor(
    private val refreshApiService: RefreshApiService,
    private val secureTokenStorage: SecureTokenStorage,
    private val userDao: UserDao,
    private val sessionManager: SessionManager
) : Authenticator {

    private val mutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? {
        Log.w("API_DEBUGGER", "[TokenAuthenticator] Received 401 Unauthorized for request: ${response.request.url.encodedPath}")

        // Break infinite retry loops — only allow 1 retry per original failed request
        if (responseCount(response) > 1) {
            Log.e("API_DEBUGGER", "[TokenAuthenticator] Request count > 1. Aborting retry loop and logging out.")
            handleRefreshFailure()
            return null
        }

        return runBlocking {
            mutex.withLock {
                // Read the access token that was used for the failed request
                val tokenUsedForFailedRequest = response.request
                    .header("Authorization")
                    ?.removePrefix("Bearer ")
                    ?.trim()

                // If token in storage already changed while waiting on the mutex,
                // another thread refreshed it — retry with the updated token immediately.
                val currentStoredToken = secureTokenStorage.getAccessToken()
                if (!currentStoredToken.isNullOrBlank() &&
                    currentStoredToken != tokenUsedForFailedRequest
                ) {
                    Log.d("API_DEBUGGER", "[TokenAuthenticator] Token was already refreshed by another thread. Retrying request: ${response.request.url.encodedPath}")
                    return@withLock response.request.newBuilder()
                        .removeHeader("Authorization")
                        .addHeader("Authorization", "Bearer $currentStoredToken")
                        .build()
                }

                // Retrieve stored refresh token from KeyStore storage
                val refreshToken = secureTokenStorage.getRefreshToken()
                if (refreshToken.isNullOrBlank()) {
                    Log.e("API_DEBUGGER", "[TokenAuthenticator] No refresh token available in storage. Logging out.")
                    handleRefreshFailure()
                    return@withLock null
                }

                try {
                    Log.d("API_DEBUGGER", "[TokenAuthenticator] Sending refresh token request to API...")
                    val currentAccessToken = secureTokenStorage.getAccessToken() ?: ""
                    val refreshResponse = refreshApiService.refreshToken(
                        authorization = "Bearer $currentAccessToken",
                        body = RefreshTokenRequest(refreshToken = refreshToken)
                    )

                    if (refreshResponse.isSuccessful) {
                        val responseBody = refreshResponse.body()
                        val tokensDto = responseBody?.data?.tokens
                        val newAccess = tokensDto?.accessToken ?: tokensDto?.realAccessToken
                        // Retain existing refreshToken if server does not send a rotated one
                        val newRefresh = tokensDto?.refreshToken?.ifBlank { null } ?: refreshToken

                        if (!newAccess.isNullOrBlank()) {
                            Log.d("API_DEBUGGER", "[TokenAuthenticator] Token refresh successful! Retrying request ${response.request.url.encodedPath} with new access token: $newAccess")
                            
                            // 1. Replace existing access token in EncryptedSharedPreferences (Android KeyStore) with refreshed token
                            secureTokenStorage.updateAccessToken(newAccess)
                            if (!newRefresh.isNullOrBlank() && newRefresh != refreshToken) {
                                secureTokenStorage.saveTokens(newAccess, newRefresh)
                            }

                            // 2. Update local Room DB user entity with replaced access token so DB state stays in sync
                            userDao.updateTokens(newAccess, newRefresh)

                            // 3. Re-call / retry the failed API request with new access token header
                            return@withLock response.request.newBuilder()
                                .removeHeader("Authorization")
                                .addHeader("Authorization", "Bearer $newAccess")
                                .build()
                        } else {
                            Log.e("API_DEBUGGER", "[TokenAuthenticator] Refresh API returned 200 OK but empty accessToken. Message: ${responseBody?.message}")
                            handleRefreshFailure()
                            return@withLock null
                        }
                    } else if (refreshResponse.code() in listOf(400, 401, 403)) {
                        Log.e("API_DEBUGGER", "[TokenAuthenticator] Refresh token expired/rejected with status ${refreshResponse.code()}. Logging out user.")
                        // Refresh token API explicitly failed with 401/400/403 -> Clear KeyStore tokens and trigger logout
                        handleRefreshFailure()
                        return@withLock null
                    } else {
                        Log.w("API_DEBUGGER", "[TokenAuthenticator] Refresh token server error status: ${refreshResponse.code()}. Retaining session.")
                        // Other HTTP status (e.g. 500/503 server error) -> do not log out user
                        return@withLock null
                    }
                } catch (e: IOException) {
                    Log.w("API_DEBUGGER", "[TokenAuthenticator] Network exception during refresh: ${e.message}. Retaining session.")
                    // Transient network exception (e.g. offline) -> do not log out user
                    return@withLock null
                } catch (e: Exception) {
                    Log.e("API_DEBUGGER", "[TokenAuthenticator] Exception during refresh: ${e.message}", e)
                    handleRefreshFailure()
                    return@withLock null
                }
            }
        }
    }

    /**
     * Clears all stored tokens from EncryptedSharedPreferences and signals
     * the UI to route the user back to the login screen.
     */
    private fun handleRefreshFailure() {
        if (secureTokenStorage.hasValidTokens()) {
            secureTokenStorage.clearTokens()
            sessionManager.emitUnauthorized()
        }
    }

    /**
     * Counts how many times the same request has already been retried by inspecting the response chain.
     */
    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }
}
