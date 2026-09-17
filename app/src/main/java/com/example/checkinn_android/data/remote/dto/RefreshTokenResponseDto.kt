package com.example.checkinn_android.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponseDto(
    @SerializedName("data")
    val data: RefreshTokenDataDto? = null,
    @SerializedName("error")
    val error: String? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("status")
    val status: String? = null
)

data class RefreshTokenDataDto(
    @SerializedName("tokens")
    val tokens: TokensDto? = null
)
