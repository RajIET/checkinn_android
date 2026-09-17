package com.example.checkinn_android.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("user_name")
    val username: String,
    @SerializedName("user_password")
    val password: String
)
