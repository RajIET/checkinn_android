package com.example.checkinn_android.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PermissionDto(
    @SerializedName("function_Id")
    val functionId: Int,
    @SerializedName("function_Name")
    val functionName: String,
    @SerializedName("function_Module")
    val functionModule: String,
    @SerializedName("function_Category")
    val functionCategory: String
)
