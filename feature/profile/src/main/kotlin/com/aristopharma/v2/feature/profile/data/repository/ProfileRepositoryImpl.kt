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

package com.aristopharma.v2.feature.profile.data.repository

import com.aristopharma.v2.core.preferences.data.UserPreferencesDataSource
import com.aristopharma.v2.core.utils.suspendRunCatching
import com.aristopharma.v2.feature.profile.data.mapper.toProfile
import com.aristopharma.v2.feature.profile.domain.model.Profile
import com.aristopharma.v2.feature.profile.domain.repository.ProfileRepository
import com.aristopharma.v2.firebase.auth.data.AuthDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of the ProfileRepository interface.
 *
 * @property userPreferencesDataSource Data source for user preferences.
 * @property authDataSource Data source for authentication.
 */
class ProfileRepositoryImpl @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
    private val authDataSource: AuthDataSource,
) : ProfileRepository {

    /**
     * Retrieves the user profile as a Flow.
     *
     * @return A Flow emitting the user profile.
     */
    override fun getProfile(): Flow<Profile> {
        return userPreferencesDataSource.getUserDataPreferences().map { it.toProfile() }
    }

    /**
     * Signs out the user and resets user preferences.
     *
     * @return A Result indicating the success or failure of the operation.
     */
    override suspend fun signOut(): Result<Unit> {
        return suspendRunCatching {
            authDataSource.signOut()
            userPreferencesDataSource.resetUserPreferences()
        }
    }
}

