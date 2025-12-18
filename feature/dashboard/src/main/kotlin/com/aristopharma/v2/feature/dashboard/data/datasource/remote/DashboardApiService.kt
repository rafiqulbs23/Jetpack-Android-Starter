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

import com.aristopharma.v2.core.ui.utils.BaseResponse
import com.aristopharma.v2.feature.dashboard.data.model.sync.FirstSyncResponse
import com.aristopharma.v2.feature.dashboard.data.model.sync.TerritoryModel
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * API service for dashboard-related endpoints.
 */
interface DashboardApiService {
    
    /**
     * Get first sync data for an employee.
     * Includes employee info, products, chemists, order types, payment options, and menu permissions.
     *
     * @param empId Employee ID
     * @return First sync response with all initial data
     */
    @GET("api/Dashboard/GetFirstSync/{empId}")
    suspend fun getFirstSync(
        @Path("empId") empId: String
    ): BaseResponse<FirstSyncResponse>
    
    /**
     * Get territory items for an employee.
     *
     * @param empId Employee ID
     * @return List of territories assigned to the employee
     */
    @GET("api/Dashboard/GetTerritoryItems/{empId}")
    suspend fun getTerritoryItems(
        @Path("empId") empId: String
    ): BaseResponse<List<TerritoryModel>>
}
