package com.example.checkinn_android.domain.usecase

import com.example.checkinn_android.domain.model.User
import com.example.checkinn_android.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLoggedInUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<User?> {
        return authRepository.getLoggedInUser()
    }
}
