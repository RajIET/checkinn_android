package com.example.checkinn_android.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("data")
    val data: LoginDataDto? = null,
    @SerializedName("error")
    val error: String? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("status")
    val status: String? = null
)

data class LoginDataDto(
    @SerializedName("permissions")
    val permissions: List<PermissionDto>? = null,
    @SerializedName("tokens")
    val tokens: TokensDto? = null,
    @SerializedName("user")
    val user: UserDto? = null
)
