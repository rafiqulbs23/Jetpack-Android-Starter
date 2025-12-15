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

package com.aristopharma.v2.feature.dashboard.data.datasource.remote.api

import com.aristopharma.v2.core.ui.utils.BaseResponse
import com.aristopharma.v2.feature.dashboard.data.model.firstSync.FirstSyncData
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API service for dashboard operations.
 */
interface DashboardApiService {
    /**
     * Gets first sync data for the given employee ID.
     *
     * @param empId The employee ID.
     * @return A [BaseResponse] containing [FirstSyncData].
     */
    @GET("/api/v1/app/sync/first-sync")
    suspend fun getFirstSync(
        @Query("empId") empId: String,
    ): BaseResponse<FirstSyncData>

    /**
     * Deletes user data from the server.
     *
     * @param empId The employee ID.
     * @return A [BaseResponse] indicating success or failure.
     */
    @DELETE("/api/v1/app/auth/userdatadelete")
    suspend fun deleteUserData(
        @Query("user") empId: String,
    ): BaseResponse<Unit>
}

