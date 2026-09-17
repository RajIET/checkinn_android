package com.example.checkinn_android.data.local.security

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

private class FakeSecureTokenStorage : SecureTokenStorage {
    private var accessToken: String? = null
    private var refreshToken: String? = null

    override fun saveTokens(accessToken: String?, refreshToken: String?) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
    }

    override fun updateAccessToken(newAccessToken: String) {
        this.accessToken = newAccessToken
    }

    override fun getAccessToken(): String? = accessToken

    override fun getRefreshToken(): String? = refreshToken

    override fun clearTokens() {
        accessToken = null
        refreshToken = null
    }

    override fun hasValidTokens(): Boolean {
        return !accessToken.isNullOrBlank() && !refreshToken.isNullOrBlank()
    }
}

class SecureTokenStorageTest {

    private lateinit var tokenStorage: SecureTokenStorage

    @Before
    fun setUp() {
        tokenStorage = FakeSecureTokenStorage()
    }

    @Test
    fun testInitialStateIsEmpty() {
        assertNull(tokenStorage.getAccessToken())
        assertNull(tokenStorage.getRefreshToken())
        assertFalse(tokenStorage.hasValidTokens())
    }

    @Test
    fun testSaveTokensStoresTokensCorrectly() {
        tokenStorage.saveTokens("mock_access_token", "mock_refresh_token")

        assertEquals("mock_access_token", tokenStorage.getAccessToken())
        assertEquals("mock_refresh_token", tokenStorage.getRefreshToken())
        assertTrue(tokenStorage.hasValidTokens())
    }

    @Test
    fun testClearTokensRemovesStoredTokens() {
        tokenStorage.saveTokens("mock_access_token", "mock_refresh_token")
        assertTrue(tokenStorage.hasValidTokens())

        tokenStorage.clearTokens()

        assertNull(tokenStorage.getAccessToken())
        assertNull(tokenStorage.getRefreshToken())
        assertFalse(tokenStorage.hasValidTokens())
    }
}
