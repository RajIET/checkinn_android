package com.example.checkinn_android.di

import com.example.checkinn_android.core.session.SessionManager
import com.example.checkinn_android.data.local.dao.UserDao
import com.example.checkinn_android.data.local.security.SecureTokenStorage
import com.example.checkinn_android.data.remote.api.AuthApiService
import com.example.checkinn_android.data.remote.api.RefreshApiService
import com.example.checkinn_android.data.remote.interceptor.AuthInterceptor
import com.example.checkinn_android.data.remote.interceptor.TokenAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

import android.util.Log

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL =
        "https://easycheckin-api-cwf7frejecbfggeb.indiasouthcentral-01.azurewebsites.net/api/"

    // ─────────────────────────────────────────────────────────────────────────
    // Shared infrastructure
    // ─────────────────────────────────────────────────────────────────────────

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor { message ->
            Log.d("API_DEBUGGER", message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        secureTokenStorage: SecureTokenStorage
    ): AuthInterceptor = AuthInterceptor(secureTokenStorage)

    // ─────────────────────────────────────────────────────────────────────────
    // Bare "refresh" OkHttpClient — NO AuthInterceptor, NO TokenAuthenticator.
    // Used exclusively by TokenAuthenticator to call POST /Auth/refresh so that
    // the refresh request can never trigger another 401 authentication cycle.
    // ─────────────────────────────────────────────────────────────────────────

    @Provides
    @Singleton
    @Named("Refresh")
    fun provideRefreshOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    @Named("Refresh")
    fun provideRefreshRetrofit(
        @Named("Refresh") okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideRefreshApiService(
        @Named("Refresh") retrofit: Retrofit
    ): RefreshApiService = retrofit.create(RefreshApiService::class.java)

    // ─────────────────────────────────────────────────────────────────────────
    // TokenAuthenticator — depends on the bare RefreshApiService
    // ─────────────────────────────────────────────────────────────────────────

    @Provides
    @Singleton
    fun provideTokenAuthenticator(
        refreshApiService: RefreshApiService,
        secureTokenStorage: SecureTokenStorage,
        userDao: UserDao,
        sessionManager: SessionManager
    ): TokenAuthenticator = TokenAuthenticator(
        refreshApiService = refreshApiService,
        secureTokenStorage = secureTokenStorage,
        userDao = userDao,
        sessionManager = sessionManager
    )

    // ─────────────────────────────────────────────────────────────────────────
    // Main OkHttpClient — AuthInterceptor + TokenAuthenticator attached
    // ─────────────────────────────────────────────────────────────────────────

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)       // log all traffic
            .addInterceptor(authInterceptor)           // attach Bearer token
            .authenticator(tokenAuthenticator)         // handle 401 → refresh
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

    // ─────────────────────────────────────────────────────────────────────────
    // Main Retrofit + AuthApiService — uses the main OkHttpClient
    // ─────────────────────────────────────────────────────────────────────────

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService =
        retrofit.create(AuthApiService::class.java)
}
