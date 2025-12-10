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

package com.aristopharma.v2.feature.profile.data.datasource.remote

import com.aristopharma.v2.feature.profile.data.datasource.remote.api.ProfileApiService
import javax.inject.Inject

/**
 * Remote data source for profile feature operations using API services.
 *
 * @param apiService The Retrofit API service for profile endpoints.
 */
class ProfileRemoteDataSource @Inject constructor(
    private val apiService: ProfileApiService,
) {
    /**
     * Sign out the current user.
     */
    suspend fun signOut() {
        val response = apiService.signOut()
        if (!response.isSuccessful) {
            throw Exception("Sign out failed: ${response.message()}")
        }
    }
}

