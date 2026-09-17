package com.example.checkinn_android.data.remote.interceptor

import com.example.checkinn_android.core.session.SessionManager
import com.example.checkinn_android.data.local.dao.UserDao
import com.example.checkinn_android.data.local.security.SecureTokenStorage
import com.example.checkinn_android.data.remote.api.RefreshApiService
import com.example.checkinn_android.data.remote.dto.RefreshTokenDataDto
import com.example.checkinn_android.data.remote.dto.RefreshTokenRequest
import com.example.checkinn_android.data.remote.dto.RefreshTokenResponseDto
import com.example.checkinn_android.data.remote.dto.TokensDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.test.runTest
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import retrofit2.Response as RetrofitResponse

@OptIn(ExperimentalCoroutinesApi::class)
class TokenAuthenticatorTest {

    private val refreshApiService: RefreshApiService = mockk()
    private val tokenStorage: SecureTokenStorage = mockk(relaxed = true)
    private val userDao: UserDao = mockk(relaxed = true)
    private val sessionManager: SessionManager = mockk(relaxed = true)
    private lateinit var authenticator: TokenAuthenticator

    @Before
    fun setUp() {
        authenticator = TokenAuthenticator(refreshApiService, tokenStorage, userDao, sessionManager)
    }

    @Test
    fun `happy path - successful refresh and retry`() = runTest {
        // Given
        val oldAccess = "old_access"
        val oldRefresh = "old_refresh"
        val newAccess = "new_access"
        val newRefresh = "new_refresh"

        every { tokenStorage.getAccessToken() } returns oldAccess
        every { tokenStorage.getRefreshToken() } returns oldRefresh
        every { tokenStorage.hasValidTokens() } returns true

        val request = Request.Builder()
            .url("https://example.com/api/data")
            .header("Authorization", "Bearer $oldAccess")
            .build()

        val response = create401Response(request)

        val responseDto = RefreshTokenResponseDto(
            data = RefreshTokenDataDto(tokens = TokensDto(accessToken = newAccess, refreshToken = newRefresh))
        )
        coEvery { 
            refreshApiService.refreshToken(any(), any()) 
        } returns RetrofitResponse.success(responseDto)

        // When
        val resultRequest = authenticator.authenticate(null, response)

        // Then
        verify { tokenStorage.updateAccessToken(newAccess) }
        assertEquals("Bearer $newAccess", resultRequest?.header("Authorization"))
    }

    @Test
    fun `concurrent 401s - only one refresh call is made`() = runTest {
        // Given
        val oldAccess = "old_access"
        val oldRefresh = "old_refresh"
        val newAccess = "new_access"
        val newRefresh = "new_refresh"

        every { tokenStorage.getAccessToken() } returnsMany listOf(oldAccess, oldAccess, newAccess)
        every { tokenStorage.getRefreshToken() } returns oldRefresh
        every { tokenStorage.hasValidTokens() } returns true

        val request = Request.Builder()
            .url("https://example.com/api/data")
            .header("Authorization", "Bearer $oldAccess")
            .build()
        val response = create401Response(request)

        val responseDto = RefreshTokenResponseDto(
            data = RefreshTokenDataDto(tokens = TokensDto(accessToken = newAccess, refreshToken = newRefresh))
        )
        coEvery { 
            refreshApiService.refreshToken(any(), any()) 
        } coAnswers {
            kotlinx.coroutines.delay(100)
            RetrofitResponse.success(responseDto)
        }

        // When
        val deferred1 = async { authenticator.authenticate(null, response) }
        val deferred2 = async { authenticator.authenticate(null, response) }

        val result1 = deferred1.await()
        val result2 = deferred2.await()

        // Then
        coVerify(exactly = 1) { refreshApiService.refreshToken(any(), any()) }
        assertEquals("Bearer $newAccess", result1?.header("Authorization"))
        assertEquals("Bearer $newAccess", result2?.header("Authorization"))
    }

    @Test
    fun `refresh failure - tokens cleared and logout emitted`() = runTest {
        // Given
        val oldAccess = "old_access"
        every { tokenStorage.getAccessToken() } returns oldAccess
        every { tokenStorage.getRefreshToken() } returns "old_refresh"
        every { tokenStorage.hasValidTokens() } returns true

        val request = Request.Builder()
            .url("https://example.com/api/data")
            .header("Authorization", "Bearer $oldAccess")
            .build()
        val response = create401Response(request)

        coEvery { 
            refreshApiService.refreshToken(any(), any()) 
        } returns RetrofitResponse.error(401, mockk(relaxed = true))

        // When
        val resultRequest = authenticator.authenticate(null, response)

        // Then
        assertNull(resultRequest)
        verify { tokenStorage.clearTokens() }
        verify { sessionManager.emitUnauthorized() }
    }

    @Test
    fun `loop prevention - returns null after one retry`() = runTest {
        // Given
        val request = Request.Builder().url("https://example.com/api/data").build()
        
        // Create a chain of two 401 responses
        val firstResponse = create401Response(request)
        val secondResponse = create401Response(request, firstResponse)

        // When
        val resultRequest = authenticator.authenticate(null, secondResponse)

        // Then
        assertNull(resultRequest)
        coVerify(exactly = 0) { refreshApiService.refreshToken(any(), any()) }
    }

    private fun create401Response(request: Request, priorResponse: Response? = null): Response {
        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(401)
            .message("Unauthorized")
            .priorResponse(priorResponse)
            .build()
    }
}
