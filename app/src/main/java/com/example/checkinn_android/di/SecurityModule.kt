package com.example.checkinn_android.di

import com.example.checkinn_android.data.local.security.SecureTokenStorage
import com.example.checkinn_android.data.local.security.SecureTokenStorageImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SecurityModule {

    @Binds
    @Singleton
    abstract fun bindSecureTokenStorage(
        impl: SecureTokenStorageImpl
    ): SecureTokenStorage
}
