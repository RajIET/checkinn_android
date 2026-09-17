package com.example.checkinn_android.data.local.security

interface SecureTokenStorage {
    fun saveTokens(accessToken: String?, refreshToken: String?)
    fun updateAccessToken(newAccessToken: String)
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun clearTokens()
    fun hasValidTokens(): Boolean
}
