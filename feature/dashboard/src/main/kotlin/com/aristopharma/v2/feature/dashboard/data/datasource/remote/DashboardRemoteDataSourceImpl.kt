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

package com.aristopharma.v2.feature.dashboard.data.datasource.remote

import com.aristopharma.v2.feature.dashboard.data.datasource.remote.api.DashboardApiService
import com.aristopharma.v2.feature.dashboard.data.model.firstSync.FirstSyncData
import javax.inject.Inject

/**
 * Implementation of [DashboardRemoteDataSource] using Retrofit API service.
 *
 * @param apiService The Retrofit API service for dashboard operations.
 */
class DashboardRemoteDataSourceImpl @Inject constructor(
    private val apiService: DashboardApiService,
) : DashboardRemoteDataSource {

    override suspend fun getFirstSync(empId: String): FirstSyncData? {
        return try {
            val response = apiService.getFirstSync(empId)
            // TODO: Handle response validation and error cases
            response.data
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun deleteUserData(empId: String) {
        try {
            apiService.deleteUserData(empId)
        } catch (e: Exception) {
            // Handle error
        }
    }
}

