package com.example.checkinn_android.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TokensDto(
    @SerializedName("accessToken")
    val accessToken: String? = null,
    @SerializedName("accessTokenExpiresAt")
    val accessTokenExpiresAt: String? = null,
    @SerializedName("refreshToken")
    val refreshToken: String? = null,
    @SerializedName("refreshTokenExpiresAt")
    val refreshTokenExpiresAt: String? = null,
    @SerializedName("data")
    val data: TokensDataDto? = null,
    @SerializedName("tokens")
    val tokens: TokensDto? = null
) {
    val realAccessToken: String?
        get() = accessToken
            ?: tokens?.accessToken
            ?: data?.accessToken
            ?: data?.tokens?.accessToken

    val realRefreshToken: String?
        get() = refreshToken
            ?: tokens?.refreshToken
            ?: data?.refreshToken
            ?: data?.tokens?.refreshToken
}

data class TokensDataDto(
    @SerializedName("accessToken")
    val accessToken: String? = null,
    @SerializedName("accessTokenExpiresAt")
    val accessTokenExpiresAt: String? = null,
    @SerializedName("refreshToken")
    val refreshToken: String? = null,
    @SerializedName("refreshTokenExpiresAt")
    val refreshTokenExpiresAt: String? = null,
    @SerializedName("tokens")
    val tokens: TokensDto? = null
)
