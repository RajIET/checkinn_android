package com.example.checkinn_android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: Int = 1,
    val firstName: String? = null,
    val lastName: String? = null,
    val username: String? = null,
    val roleId: Int? = null,
    val role: String? = null,
    val hotelId: Int? = null,
    val hotelName: String? = null,
    val hotelCode: String? = null,
    val hotelAddress: String? = null,
    val hotelContactNo: String? = null,
    val hotelContact2: String? = null,
    val hotelCategory: String? = null,
    val hotelGroup: String? = null,
    val hotelOwner: String? = null,
    val hotelRating: Int? = null,
    val hotelRemarks: String? = null,
    val hotelQRCode: String? = null,
    val cityId: Int? = null,
    val city: String? = null,
    val state: String? = null,
    val country: String? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val isLoggedIn: Boolean = true
)
