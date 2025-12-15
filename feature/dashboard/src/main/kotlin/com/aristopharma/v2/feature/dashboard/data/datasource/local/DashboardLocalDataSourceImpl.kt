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

import com.aristopharma.v2.core.preferences.data.UserPreferencesDataSource
import com.aristopharma.v2.feature.dashboard.data.model.DashboardSummary
import com.aristopharma.v2.feature.dashboard.data.model.MenuPermission
import com.aristopharma.v2.feature.dashboard.data.model.firstSync.FirstSyncData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of [DashboardLocalDataSource] using Room database and DataStore.
 *
 * @param userPreferencesDataSource The user preferences data source.
 */
class DashboardLocalDataSourceImpl @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
    // TODO: Add Room DAO when available
) : DashboardLocalDataSource {

    override fun getMenuPermissions(): Flow<List<MenuPermission>> {
        // TODO: Implement using Room DAO
        return kotlinx.coroutines.flow.flowOf(emptyList())
    }

    override suspend fun getDashboardSummary(): DashboardSummary? {
        // TODO: Implement using DataStore or SharedPreferences
        return null
    }

    override suspend fun saveDashboardSummary(summary: DashboardSummary) {
        // TODO: Implement using DataStore or SharedPreferences
    }

    override suspend fun saveFirstSyncData(firstSyncData: FirstSyncData) {
        // TODO: Implement saving first sync data to Room database
    }

    override suspend fun hasPendingOrderApprovals(): Boolean {
        // TODO: Implement checking for pending order approvals
        return false
    }

    override suspend fun clearLoginState() {
        // TODO: Implement clearing login state
    }
}

