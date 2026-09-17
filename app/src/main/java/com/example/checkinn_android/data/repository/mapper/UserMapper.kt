package com.example.checkinn_android.data.repository.mapper

import com.example.checkinn_android.data.local.entity.PermissionEntity
import com.example.checkinn_android.data.local.entity.UserEntity
import com.example.checkinn_android.data.remote.dto.PermissionDto
import com.example.checkinn_android.data.remote.dto.UserDto
import com.example.checkinn_android.domain.model.Permission
import com.example.checkinn_android.domain.model.User

fun UserDto.toEntity(
    accessToken: String? = null,
    refreshToken: String? = null,
    isLoggedIn: Boolean = true
): UserEntity {
    return UserEntity(
        id = id ?: 1,
        firstName = firstName,
        lastName = lastName,
        username = username,
        roleId = roleId,
        role = role,
        hotelId = hotelId,
        hotelName = hotelName,
        hotelCode = hotelCode,
        hotelAddress = hotelAddress,
        hotelContactNo = hotelContactNo,
        hotelContact2 = hotelContact2,
        hotelCategory = hotelCategory,
        hotelGroup = hotelGroup,
        hotelOwner = hotelOwner,
        hotelRating = hotelRating,
        hotelRemarks = hotelRemarks,
        hotelQRCode = hotelQRCode,
        cityId = cityId,
        city = city,
        state = state,
        country = country,
        accessToken = accessToken,
        refreshToken = refreshToken,
        isLoggedIn = isLoggedIn
    )
}

fun UserEntity.toDomain(): User {
    return User(
        id = id,
        firstName = firstName ?: "",
        lastName = lastName ?: "",
        username = username ?: "",
        roleId = roleId,
        role = role,
        hotelId = hotelId,
        hotelName = hotelName,
        hotelCode = hotelCode,
        hotelAddress = hotelAddress,
        hotelContactNo = hotelContactNo,
        hotelContact2 = hotelContact2,
        hotelCategory = hotelCategory,
        hotelGroup = hotelGroup,
        hotelOwner = hotelOwner,
        hotelRating = hotelRating,
        hotelRemarks = hotelRemarks,
        hotelQRCode = hotelQRCode,
        cityId = cityId,
        city = city,
        state = state,
        country = country,
        accessToken = accessToken,
        refreshToken = refreshToken,
        isLoggedIn = isLoggedIn
    )
}

fun PermissionDto.toEntity(): PermissionEntity {
    return PermissionEntity(
        functionId = functionId,
        functionName = functionName,
        functionModule = functionModule,
        functionCategory = functionCategory
    )
}

fun PermissionEntity.toDomain(): Permission {
    return Permission(
        functionId = functionId,
        functionName = functionName,
        functionModule = functionModule,
        functionCategory = functionCategory
    )
}
