package com.example.checkinn_android.domain.usecase

import com.example.checkinn_android.core.common.Resource
import com.example.checkinn_android.domain.model.User
import com.example.checkinn_android.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String, password: String): Flow<Resource<User>> {
        return authRepository.login(email.trim(), password)
    }
}
