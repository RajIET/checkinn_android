package com.example.checkinn_android.data.repository

import com.example.checkinn_android.core.common.DispatcherProvider
import com.example.checkinn_android.core.common.Resource
import com.example.checkinn_android.data.local.dao.UserDao
import com.example.checkinn_android.data.local.security.SecureTokenStorage
import com.example.checkinn_android.data.remote.api.AuthApiService
import com.example.checkinn_android.data.remote.dto.LoginRequest
import com.example.checkinn_android.data.repository.mapper.toDomain
import com.example.checkinn_android.data.repository.mapper.toEntity
import com.example.checkinn_android.domain.model.User
import com.example.checkinn_android.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val userDao: UserDao,
    private val secureTokenStorage: SecureTokenStorage,
    private val dispatchers: DispatcherProvider
) : AuthRepository {

    override fun login(email: String, password: String): Flow<Resource<User>> = flow {
        emit(Resource.Loading)
        try {
            val response = authApiService.login(
                LoginRequest(
                    username = email.trim(),
                    password = password
                )
            )

            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!
                val loginData = loginResponse.data
                val userDto = loginData?.user

                if (userDto != null) {
                    val accessToken = loginData.tokens?.accessToken
                    val refreshToken = loginData.tokens?.refreshToken

                    // Store tokens securely in Android KeyStore
                    secureTokenStorage.saveTokens(accessToken, refreshToken)

                    val userEntity = userDto.toEntity(
                        accessToken = accessToken,
                        refreshToken = refreshToken,
                        isLoggedIn = true
                    )
                    val permissionEntities = loginData.permissions?.map { it.toEntity() } ?: emptyList()

                    userDao.saveUserAndPermissions(userEntity, permissionEntities)
                    emit(Resource.Success(userEntity.toDomain()))
                } else {
                    val message = loginResponse.error ?: loginResponse.message ?: "Authentication failed"
                    emit(Resource.Error(message))
                }
            } else {
                emit(Resource.Error("Your username or password is incorrect. Please try again."))
            }
        } catch (e: IOException) {
            emit(Resource.Error("Network error. Please check your internet connection.", e))
        } catch (e: HttpException) {
            emit(Resource.Error("Your username or password is incorrect. Please try again.", e))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred", e))
        }
    }.flowOn(dispatchers.io)

    override fun getLoggedInUser(): Flow<User?> {
        return userDao.getLoggedInUser()
            .map { entity -> entity?.toDomain() }
            .flowOn(dispatchers.io)
    }

    override suspend fun logout() {
        withContext(dispatchers.io) {
            secureTokenStorage.clearTokens()
            userDao.logout()
        }
    }
}
