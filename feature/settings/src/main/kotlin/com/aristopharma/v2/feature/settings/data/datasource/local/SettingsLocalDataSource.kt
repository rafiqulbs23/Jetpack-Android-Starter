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

package com.aristopharma.v2.feature.settings.data.datasource.local

import com.aristopharma.v2.core.preferences.data.UserPreferencesDataSource
import com.aristopharma.v2.core.preferences.model.DarkThemeConfigPreferences
import com.aristopharma.v2.core.preferences.model.UserDataPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Local data source for settings feature operations.
 *
 * @param userPreferencesDataSource The user preferences data source.
 */
class SettingsLocalDataSource @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
) {
    /**
     * Get user settings data from local storage.
     *
     * @return A Flow of user data preferences.
     */
    fun getUserSettings(): Flow<UserDataPreferences> {
        return userPreferencesDataSource.getUserDataPreferences()
    }

    /**
     * Set the dark theme configuration.
     *
     * @param darkThemeConfig The dark theme configuration.
     */
    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfigPreferences) {
        userPreferencesDataSource.setDarkThemeConfig(darkThemeConfig)
    }

    /**
     * Set the dynamic color preference.
     *
     * @param useDynamicColor Whether to use dynamic colors.
     */
    suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        userPreferencesDataSource.setDynamicColorPreference(useDynamicColor)
    }

    /**
     * Reset user preferences (used on sign out).
     */
    suspend fun resetUserPreferences() {
        userPreferencesDataSource.resetUserPreferences()
    }
}

