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

import com.aristopharma.v2.core.utils.suspendRunCatching
import com.aristopharma.v2.feature.profile.data.datasource.local.ProfileLocalDataSource
import com.aristopharma.v2.feature.profile.data.datasource.remote.ProfileRemoteDataSource
import com.aristopharma.v2.feature.profile.data.mapper.toProfile
import com.aristopharma.v2.feature.profile.domain.model.Profile
import com.aristopharma.v2.feature.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of the ProfileRepository interface.
 *
 * @param localDataSource The local data source for profile data.
 * @param remoteDataSource The remote data source for authentication operations.
 */
class ProfileRepositoryImpl @Inject constructor(
    private val localDataSource: ProfileLocalDataSource,
    private val remoteDataSource: ProfileRemoteDataSource,
) : ProfileRepository {

    /**
     * Retrieves the user profile as a Flow.
     *
     * @return A Flow emitting the user profile.
     */
    override fun getProfile(): Flow<Profile> {
        return localDataSource.getUserProfile().map { it.toProfile() }
    }

    /**
     * Signs out the user and resets user preferences.
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

