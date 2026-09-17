package com.example.checkinn_android.data.repository.mapper

import com.example.checkinn_android.data.local.entity.PermissionEntity
import com.example.checkinn_android.data.local.entity.UserEntity
import com.example.checkinn_android.data.remote.dto.PermissionDto
import com.example.checkinn_android.data.remote.dto.UserDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UserMapperTest {

    @Test
    fun `toEntity maps UserDto to UserEntity correctly`() {
        val dto = UserDto(
            id = 101,
            firstName = "Raj",
            lastName = "Sharma",
            username = "raj.sharma@example.com",
            role = "Manager",
            hotelId = 5,
            hotelName = "Grand CheckInn"
        )
        val entity = dto.toEntity(accessToken = "access-token-123", refreshToken = "refresh-token-456", isLoggedIn = true)

        assertEquals(101, entity.id)
        assertEquals("Raj", entity.firstName)
        assertEquals("Sharma", entity.lastName)
        assertEquals("raj.sharma@example.com", entity.username)
        assertEquals("Grand CheckInn", entity.hotelName)
        assertEquals("access-token-123", entity.accessToken)
        assertEquals("refresh-token-456", entity.refreshToken)
        assertTrue(entity.isLoggedIn)
    }

    @Test
    fun `toDomain maps UserEntity to User domain model correctly`() {
        val entity = UserEntity(
            id = 102,
            firstName = "Priya",
            lastName = "Patel",
            username = "priya.patel@example.com",
            role = "Receptionist",
            hotelId = 5,
            hotelName = "Grand CheckInn",
            accessToken = "jwt-access-token",
            refreshToken = "jwt-refresh-token",
            isLoggedIn = true
        )
        val domain = entity.toDomain()

        assertEquals(102, domain.id)
        assertEquals("Priya", domain.firstName)
        assertEquals("Patel", domain.lastName)
        assertEquals("Priya Patel", domain.fullName)
        assertEquals("Grand CheckInn", domain.hotelName)
        assertEquals("jwt-access-token", domain.accessToken)
        assertTrue(domain.isLoggedIn)
    }

    @Test
    fun `maps PermissionDto and PermissionEntity correctly`() {
        val dto = PermissionDto(
            functionId = 1,
            functionName = "CheckIn_Guest",
            functionModule = "FrontDesk",
            functionCategory = "Operations"
        )
        val entity = dto.toEntity()

        assertEquals(1, entity.functionId)
        assertEquals("CheckIn_Guest", entity.functionName)
        assertEquals("FrontDesk", entity.functionModule)
        assertEquals("Operations", entity.functionCategory)

        val domain = entity.toDomain()
        assertEquals(1, domain.functionId)
        assertEquals("CheckIn_Guest", domain.functionName)
    }
}
