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
import com.aristopharma.v2.feature.dashboard.data.local.dao.DashboardDao
import com.aristopharma.v2.feature.dashboard.data.local.mapper.toEntity
import com.aristopharma.v2.feature.dashboard.data.local.mapper.toModel
import com.aristopharma.v2.feature.dashboard.data.model.DashboardSummary
import com.aristopharma.v2.feature.dashboard.data.model.MenuPermission
import com.aristopharma.v2.feature.dashboard.data.model.sync.FirstSyncResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of [DashboardLocalDataSource] using Room database.
 *
 * @param dashboardDao The dashboard DAO for database operations.
 * @param userPreferencesDataSource The user preferences data source.
 */
class DashboardLocalDataSourceImpl @Inject constructor(
    private val dashboardDao: DashboardDao,
    private val userPreferencesDataSource: UserPreferencesDataSource,
) : DashboardLocalDataSource {

    override fun getMenuPermissions(): Flow<List<MenuPermission>> {
        return dashboardDao.getMenuPermissions()
            .map { entities -> entities.map { it.toModel() } }
    }

    override suspend fun getDashboardSummary(): DashboardSummary? {
        return dashboardDao.getDashboardSummary()?.toModel()
    }

    override suspend fun saveDashboardSummary(summary: DashboardSummary) {
        dashboardDao.insertDashboardSummary(summary.toEntity())
    }

    override suspend fun saveFirstSyncData(firstSyncData: FirstSyncResponse) {
        // Save employee info
        firstSyncData.employeeInfo?.let { employee ->
            dashboardDao.insertEmployee(employee.toEntity())
        }
        
        // Save products
        val products = firstSyncData.productInfos.map { it.toEntity() }
        if (products.isNotEmpty()) {
            dashboardDao.insertProducts(products)
        }
        
        // Save chemists
        val chemists = firstSyncData.chemistInfos.map { it.toEntity() }
        if (chemists.isNotEmpty()) {
            dashboardDao.insertChemists(chemists)
        }
        
        // Save menu permissions
        val permissions = firstSyncData.menuPermissions.map { it.toEntity() }
        if (permissions.isNotEmpty()) {
            dashboardDao.insertMenuPermissions(permissions)
        }
    }

    override suspend fun hasPendingOrderApprovals(): Boolean {
        // TODO: Implement when order feature is available
        return false
    }

    override suspend fun clearLoginState() {
        // Clear all sync data on logout
        dashboardDao.deleteAllSyncData()
    }
    
    override suspend fun deleteSyncData() {
        dashboardDao.deleteAllSyncData()
    }
}
