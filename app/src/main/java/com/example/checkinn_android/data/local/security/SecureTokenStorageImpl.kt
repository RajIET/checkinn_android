package com.example.checkinn_android.data.local.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecureTokenStorageImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SecureTokenStorage {

    @Volatile
    private var cachedAccessToken: String? = null

    @Volatile
    private var cachedRefreshToken: String? = null

    private val prefs: SharedPreferences by lazy {
        try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            EncryptedSharedPreferences.create(
                context,
                PREFS_FILENAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            context.getSharedPreferences(FALLBACK_PREFS_FILENAME, Context.MODE_PRIVATE)
        }
    }

    override fun saveTokens(accessToken: String?, refreshToken: String?) {
        cachedAccessToken = accessToken
        cachedRefreshToken = refreshToken
        prefs.edit().apply {
            putString(KEY_ACCESS_TOKEN, accessToken)
            putString(KEY_REFRESH_TOKEN, refreshToken)
            commit()
        }
    }

    override fun updateAccessToken(newAccessToken: String) {
        cachedAccessToken = newAccessToken
        prefs.edit().apply {
            putString(KEY_ACCESS_TOKEN, newAccessToken)
            commit()
        }
    }

    override fun getAccessToken(): String? {
        val cached = cachedAccessToken
        if (!cached.isNullOrBlank()) return cached
        val stored = prefs.getString(KEY_ACCESS_TOKEN, null)
        cachedAccessToken = stored
        return stored
    }

    override fun getRefreshToken(): String? {
        val cached = cachedRefreshToken
        if (!cached.isNullOrBlank()) return cached
        val stored = prefs.getString(KEY_REFRESH_TOKEN, null)
        cachedRefreshToken = stored
        return stored
    }

    override fun clearTokens() {
        cachedAccessToken = null
        cachedRefreshToken = null
        prefs.edit().apply {
            remove(KEY_ACCESS_TOKEN)
            remove(KEY_REFRESH_TOKEN)
            commit()
        }
    }

    override fun hasValidTokens(): Boolean {
        val access = getAccessToken()
        val refresh = getRefreshToken()
        return !access.isNullOrBlank() && !refresh.isNullOrBlank()
    }

    companion object {
        private const val PREFS_FILENAME = "checkinn_secure_tokens"
        private const val FALLBACK_PREFS_FILENAME = "checkinn_tokens_fallback"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
    }
}
