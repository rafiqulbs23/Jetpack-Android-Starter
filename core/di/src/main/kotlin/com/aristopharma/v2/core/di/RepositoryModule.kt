/*
 * Copyright 2025 Md. Rafiqul Islam
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aristopharma.v2.core.di

import com.aristopharma.v2.feature.auth.data.repository.AuthRepositoryImpl
import com.aristopharma.v2.feature.auth.domain.repository.AuthRepository
import com.aristopharma.v2.feature.home.data.repository.HomeRepositoryImpl
import com.aristopharma.v2.feature.home.domain.repository.HomeRepository
import com.aristopharma.v2.feature.profile.data.repository.ProfileRepositoryImpl
import com.aristopharma.v2.feature.profile.domain.repository.ProfileRepository
import com.aristopharma.v2.feature.settings.data.repository.SettingsRepositoryImpl
import com.aristopharma.v2.feature.settings.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger module for providing repository implementations for all features.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    /**
     * Binds the implementation of [AuthRepository] to [AuthRepositoryImpl].
     *
     * @param authRepositoryImpl The implementation of [AuthRepository].
     * @return The bound [AuthRepository] instance.
     */
    @Binds
    @Singleton
    internal abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl,
    ): AuthRepository

    /**
     * Binds the implementation of [HomeRepository] to [HomeRepositoryImpl].
     *
     * @param homeRepositoryImpl The implementation of [HomeRepository].
     * @return The bound [HomeRepository] instance.
     */
    @Binds
    @Singleton
    internal abstract fun bindHomeRepository(
        homeRepositoryImpl: HomeRepositoryImpl,
    ): HomeRepository

    /**
     * Binds the implementation of [ProfileRepository] to [ProfileRepositoryImpl].
     *
     * @param profileRepositoryImpl The implementation of [ProfileRepository].
     * @return The bound [ProfileRepository] instance.
     */
    @Binds
    @Singleton
    internal abstract fun bindProfileRepository(
        profileRepositoryImpl: ProfileRepositoryImpl,
    ): ProfileRepository

    /**
     * Binds the implementation of [SettingsRepository] to [SettingsRepositoryImpl].
     *
     * @param settingsRepositoryImpl The implementation of [SettingsRepository].
     * @return The bound [SettingsRepository] instance.
     */
    @Binds
    @Singleton
    internal abstract fun bindSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl,
    ): SettingsRepository
}
