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

package com.aristopharma.v2.feature.profile.data.datasource.local

import com.aristopharma.v2.core.preferences.data.UserPreferencesDataSource
import com.aristopharma.v2.core.preferences.model.PreferencesUserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Local data source for profile feature operations.
 *
 * @param userPreferencesDataSource The user preferences data source.
 */
class ProfileLocalDataSource @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
) {
    /**
     * Get user profile data from local storage.
     *
     * @return A Flow of user profile preferences.
     */
    fun getUserProfile(): Flow<PreferencesUserProfile> {
        return userPreferencesDataSource.getUserDataPreferences().map { userData ->
            PreferencesUserProfile(
                id = userData.id,
                userName = userData.userName ?: "",
                profilePictureUriString = userData.profilePictureUriString,
            )
        }
    }

    /**
     * Reset user preferences (used on sign out).
     */
    suspend fun resetUserPreferences() {
        userPreferencesDataSource.resetUserPreferences()
    }
}

