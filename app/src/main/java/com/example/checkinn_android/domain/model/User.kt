package com.example.checkinn_android.domain.model

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
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
) {
    val fullName: String
        get() = listOfNotNull(
            firstName.takeIf { it.isNotBlank() },
            lastName.takeIf { it.isNotBlank() }
        ).joinToString(" ").ifEmpty { username }
}
