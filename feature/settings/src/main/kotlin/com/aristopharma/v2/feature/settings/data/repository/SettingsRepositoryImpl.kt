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

package com.aristopharma.v2.feature.settings.data.repository

import com.aristopharma.v2.core.utils.suspendRunCatching
import com.aristopharma.v2.feature.settings.data.datasource.local.SettingsLocalDataSource
import com.aristopharma.v2.feature.settings.data.datasource.remote.SettingsRemoteDataSource
import com.aristopharma.v2.feature.settings.data.mapper.asSettings
import com.aristopharma.v2.feature.settings.data.mapper.toDarkThemeConfigPreferences
import com.aristopharma.v2.feature.settings.domain.model.DarkThemeConfig
import com.aristopharma.v2.feature.settings.domain.model.Settings
import com.aristopharma.v2.feature.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of [SettingsRepository].
 *
 * @param localDataSource The local data source for settings data.
 * @param remoteDataSource The remote data source for authentication operations.
 */
class SettingsRepositoryImpl @Inject constructor(
    private val localDataSource: SettingsLocalDataSource,
    private val remoteDataSource: SettingsRemoteDataSource,
) : SettingsRepository {

    /**
     * Retrieves the user's settings.
     *
     * @return A Flow emitting the user's settings.
     */
    override fun getSettings(): Flow<Settings> =
        localDataSource.getUserSettings().map { it.asSettings() }

    /**
     * Sets the dark theme configuration.
     *
     * @param darkThemeConfig The dark theme configuration.
     * @return A Result indicating the success or failure of the operation.
     */
    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig): Result<Unit> {
        return suspendRunCatching {
            localDataSource.setDarkThemeConfig(
                darkThemeConfig.toDarkThemeConfigPreferences(),
            )
        }
    }

    /**
     * Sets the dynamic color preference.
     *
     * @param useDynamicColor Whether to use dynamic colors.
     * @return A Result indicating the success or failure of the operation.
     */
    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean): Result<Unit> {
        return suspendRunCatching {
            localDataSource.setDynamicColorPreference(useDynamicColor)
        }
    }

    /**
     * Signs out the current user.
     *
     * @return A Result indicating the success or failure of the operation.
     */
    override suspend fun signOut(): Result<Unit> {
        return suspendRunCatching {
            remoteDataSource.signOut()
            localDataSource.resetUserPreferences()
        }
    }
}

