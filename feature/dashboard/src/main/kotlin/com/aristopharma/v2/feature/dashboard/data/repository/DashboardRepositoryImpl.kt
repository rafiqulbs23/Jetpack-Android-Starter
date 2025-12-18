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

package com.aristopharma.v2.feature.dashboard.data.repository

import com.aristopharma.v2.core.preferences.data.UserPreferencesDataSource
import com.aristopharma.v2.core.utils.suspendRunCatching
import com.aristopharma.v2.feature.dashboard.data.datasource.local.DashboardLocalDataSource
import com.aristopharma.v2.feature.dashboard.data.datasource.remote.DashboardRemoteDataSource
import com.aristopharma.v2.feature.dashboard.data.model.DashboardSummary
import com.aristopharma.v2.feature.dashboard.data.model.MenuPermission
import com.aristopharma.v2.feature.dashboard.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of [DashboardRepository] responsible for handling dashboard operations.
 *
 * @param remoteDataSource The remote data source for dashboard operations.
 * @param localDataSource The local data source for dashboard operations.
 * @param userPreferencesDataSource The user preferences data source.
 */
class DashboardRepositoryImpl @Inject constructor(
    private val remoteDataSource: DashboardRemoteDataSource,
    private val localDataSource: DashboardLocalDataSource,
    private val userPreferencesDataSource: UserPreferencesDataSource,
) : DashboardRepository {

    override fun getMenuPermissions(): Flow<List<MenuPermission>> {
        return localDataSource.getMenuPermissions()
    }

    override suspend fun performFirstSync(empId: String): Result<Unit> {
        return suspendRunCatching {
            val syncResult = remoteDataSource.getFirstSync(empId)
            syncResult?.let { firstSyncData ->
                // Save sync data to local storage
                localDataSource.saveFirstSyncData(firstSyncData)
                
                // Update dashboard summary
                val summary = DashboardSummary(
                    employeeName = firstSyncData.employeeInfo?.surName ?: "",
                    employeeId = firstSyncData.employeeInfo?.empId?.toString() ?: "",
                    isFirstSyncDone = true,
                )
                saveDashboardSummary(summary)
            } ?: throw IllegalStateException("First sync failed: No data received")
        }
    }

    override suspend fun getDashboardSummary(): DashboardSummary? {
        return localDataSource.getDashboardSummary()
    }

    override suspend fun saveDashboardSummary(summary: DashboardSummary) {
        localDataSource.saveDashboardSummary(summary)
    }

    override suspend fun hasPendingOrderApprovals(): Boolean {
        return localDataSource.hasPendingOrderApprovals()
    }

    override suspend fun deviceLogout() {
        localDataSource.clearLoginState()
    }

    override suspend fun deleteUserData(empId: String): Result<Unit> {
        return suspendRunCatching {
            remoteDataSource.deleteUserData(empId)
        }
    }
    
    override suspend fun getTerritoryItems(empId: String): Result<List<com.aristopharma.v2.feature.dashboard.data.model.sync.TerritoryModel>> {
        return suspendRunCatching {
            remoteDataSource.getTerritoryItems(empId)
        }
    }
    
    override suspend fun saveFirstSyncData(data: com.aristopharma.v2.feature.dashboard.data.model.sync.FirstSyncResponse) {
        localDataSource.saveFirstSyncData(data)
    }
    
    override suspend fun isFirstSyncDone(): Boolean {
        val summary = getDashboardSummary()
        return summary?.isFirstSyncDone ?: false
    }
    
    override suspend fun deleteSyncData() {
        localDataSource.deleteSyncData()
    }
}

