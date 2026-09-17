package com.example.checkinn_android.domain.repository

import com.example.checkinn_android.core.common.Resource
import com.example.checkinn_android.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(email: String, password: String): Flow<Resource<User>>
    fun getLoggedInUser(): Flow<User?>
    suspend fun logout()
}
