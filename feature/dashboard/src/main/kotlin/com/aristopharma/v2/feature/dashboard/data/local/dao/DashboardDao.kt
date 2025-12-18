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

package com.aristopharma.v2.feature.dashboard.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.aristopharma.v2.feature.dashboard.data.local.entity.ChemistInfoEntity
import com.aristopharma.v2.feature.dashboard.data.local.entity.DashboardSummaryEntity
import com.aristopharma.v2.feature.dashboard.data.local.entity.EmployeeInfoEntity
import com.aristopharma.v2.feature.dashboard.data.local.entity.MenuPermissionEntity
import com.aristopharma.v2.feature.dashboard.data.local.entity.ProductInfoEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for dashboard operations.
 */
@Dao
interface DashboardDao {
    
    // Employee Operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployee(employee: EmployeeInfoEntity)
    
    @Query("SELECT * FROM employee_info LIMIT 1")
    suspend fun getEmployee(): EmployeeInfoEntity?
    
    @Query("DELETE FROM employee_info")
    suspend fun deleteEmployeeInfo()
    
    // Product Operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductInfoEntity>)
    
    @Query("SELECT * FROM product_info")
    suspend fun getAllProducts(): List<ProductInfoEntity>
    
    @Query("SELECT * FROM product_info WHERE productId = :productId")
    suspend fun getProductById(productId: String): ProductInfoEntity?
    
    @Query("DELETE FROM product_info")
    suspend fun deleteProducts()
    
    // Chemist Operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChemists(chemists: List<ChemistInfoEntity>)
    
    @Query("SELECT * FROM chemist_info")
    suspend fun getAllChemists(): List<ChemistInfoEntity>
    
    @Query("SELECT * FROM chemist_info WHERE chemistId = :chemistId")
    suspend fun getChemistById(chemistId: String): ChemistInfoEntity?
    
    @Query("DELETE FROM chemist_info")
    suspend fun deleteChemists()
    
    // Menu Permission Operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuPermissions(permissions: List<MenuPermissionEntity>)
    
    @Query("SELECT * FROM menu_permissions ORDER BY sequence ASC")
    fun getMenuPermissions(): Flow<List<MenuPermissionEntity>>
    
    @Query("DELETE FROM menu_permissions")
    suspend fun deleteMenuPermissions()
    
    // Dashboard Summary Operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDashboardSummary(summary: DashboardSummaryEntity)
    
    @Query("SELECT * FROM dashboard_summary WHERE id = 1")
    suspend fun getDashboardSummary(): DashboardSummaryEntity?
    
    @Query("DELETE FROM dashboard_summary")
    suspend fun deleteDashboardSummary()
    
    // Transaction: Delete all sync data
    @Transaction
    suspend fun deleteAllSyncData() {
        deleteEmployeeInfo()
        deleteProducts()
        deleteChemists()
        deleteMenuPermissions()
        deleteDashboardSummary()
    }
}
