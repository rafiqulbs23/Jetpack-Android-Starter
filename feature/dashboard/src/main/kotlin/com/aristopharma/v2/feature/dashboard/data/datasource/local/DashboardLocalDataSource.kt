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

package com.aristopharma.v2.feature.dashboard.data.datasource.local

import com.aristopharma.v2.feature.dashboard.data.model.DashboardSummary
import com.aristopharma.v2.feature.dashboard.data.model.MenuPermission
import com.aristopharma.v2.feature.dashboard.data.model.sync.FirstSyncResponse
import kotlinx.coroutines.flow.Flow

/**
 * Local data source interface for dashboard operations.
 */
interface DashboardLocalDataSource {
    /**
     * Gets menu permissions from local database.
     *
     * @return A Flow of list of menu permissions.
     */
    fun getMenuPermissions(): Flow<List<MenuPermission>>

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
     * Saves first sync data to local storage.
     *
     * @param firstSyncData The first sync data to save.
     */
    suspend fun saveFirstSyncData(firstSyncData: FirstSyncResponse)

    /**
     * Checks if there are pending order approvals.
     *
     * @return true if there are pending approvals, false otherwise.
     */
    suspend fun hasPendingOrderApprovals(): Boolean

    /**
     * Clears login state (used for logout).
     */
    suspend fun clearLoginState()
    
    /**
     * Deletes all sync data from local storage.
     */
    suspend fun deleteSyncData()
}

