package com.aristopharma.v2.feature.auth.di

import com.aristopharma.v2.feature.auth.data.datasource.local.AuthLocalDataSource
import com.aristopharma.v2.feature.auth.data.datasource.remote.AuthRemoteDataSource
import com.aristopharma.v2.feature.auth.data.datasource.remote.AuthRemoteDataSourceImpl
import com.aristopharma.v2.feature.auth.data.repository.AuthRepositoryImpl
import com.aristopharma.v2.feature.auth.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing authentication-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Binds
    @Singleton
    abstract fun bindAuthRemoteDataSource(
        authRemoteDataSourceImpl: AuthRemoteDataSourceImpl,
    ): AuthRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl,
    ): AuthRepository
}

