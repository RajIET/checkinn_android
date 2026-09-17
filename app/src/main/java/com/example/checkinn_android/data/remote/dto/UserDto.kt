package com.example.checkinn_android.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("firstName")
    val firstName: String? = null,
    @SerializedName("lastName")
    val lastName: String? = null,
    @SerializedName("username")
    val username: String? = null,
    @SerializedName("roleId")
    val roleId: Int? = null,
    @SerializedName("role")
    val role: String? = null,
    @SerializedName("hotelId")
    val hotelId: Int? = null,
    @SerializedName("hotelName")
    val hotelName: String? = null,
    @SerializedName("hotelCode")
    val hotelCode: String? = null,
    @SerializedName("hotelAddress")
    val hotelAddress: String? = null,
    @SerializedName("hotelContactNo")
    val hotelContactNo: String? = null,
    @SerializedName("hotelContact2")
    val hotelContact2: String? = null,
    @SerializedName("hotelCategory")
    val hotelCategory: String? = null,
    @SerializedName("hotelGroup")
    val hotelGroup: String? = null,
    @SerializedName("hotelOwner")
    val hotelOwner: String? = null,
    @SerializedName("hotelRating")
    val hotelRating: Int? = null,
    @SerializedName("hotelRemarks")
    val hotelRemarks: String? = null,
    @SerializedName("hotelQRCode")
    val hotelQRCode: String? = null,
    @SerializedName("cityId")
    val cityId: Int? = null,
    @SerializedName("city")
    val city: String? = null,
    @SerializedName("state")
    val state: String? = null,
    @SerializedName("country")
    val country: String? = null
)
