package com.example.checkinn_android.di

import com.example.checkinn_android.data.repository.AuthRepositoryImpl
import com.example.checkinn_android.data.repository.BookingRepositoryImpl
import com.example.checkinn_android.domain.repository.AuthRepository
import com.example.checkinn_android.domain.repository.BookingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindBookingRepository(
        bookingRepositoryImpl: BookingRepositoryImpl
    ): BookingRepository
}
