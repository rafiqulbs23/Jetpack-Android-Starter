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

package com.aristopharma.v2.feature.dashboard.domain.repository

import com.aristopharma.v2.feature.dashboard.data.model.DashboardSummary
import com.aristopharma.v2.feature.dashboard.data.model.MenuPermission
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for dashboard operations.
 */
interface DashboardRepository {
    /**
     * Gets menu permissions from the local database.
     *
     * @return A Flow of list of menu permissions.
     */
    fun getMenuPermissions(): Flow<List<MenuPermission>>

    /**
     * Performs first sync operation for the given employee ID.
     *
     * @param empId The employee ID.
     * @return Result indicating success or failure.
     */
    suspend fun performFirstSync(empId: String): Result<Unit>

    /**
     * Gets dashboard summary from local storage.
     *
     * @return The dashboard summary, or null if not available.
     */
    suspend fun getDashboardSummary(): DashboardSummary?

    /**
     * Saves dashboard summary to local storage.
     *
     * @param summary The dashboard summary to save.
     */
    suspend fun saveDashboardSummary(summary: DashboardSummary)

    /**
     * Checks if there are pending order approvals (for red dot indicator).
     *
     * @return true if there are pending approvals, false otherwise.
     */
    suspend fun hasPendingOrderApprovals(): Boolean

    /**
     * Logs out the user from the device.
     */
    suspend fun deviceLogout()

    /**
     * Deletes user data from the server.
     *
     * @param empId The employee ID.
     * @return Result indicating success or failure.
     */
    suspend fun deleteUserData(empId: String): Result<Unit>
}

