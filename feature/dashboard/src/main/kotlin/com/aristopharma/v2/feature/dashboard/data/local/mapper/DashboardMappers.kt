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

package com.aristopharma.v2.feature.dashboard.data.local.mapper

import com.aristopharma.v2.feature.dashboard.data.local.entity.ChemistInfoEntity
import com.aristopharma.v2.feature.dashboard.data.local.entity.DashboardSummaryEntity
import com.aristopharma.v2.feature.dashboard.data.local.entity.EmployeeInfoEntity
import com.aristopharma.v2.feature.dashboard.data.local.entity.MenuPermissionEntity
import com.aristopharma.v2.feature.dashboard.data.local.entity.ProductInfoEntity
import com.aristopharma.v2.feature.dashboard.data.model.DashboardSummary
import com.aristopharma.v2.feature.dashboard.data.model.MenuPermission
import com.aristopharma.v2.feature.dashboard.data.model.sync.ChemistInfoModel
import com.aristopharma.v2.feature.dashboard.data.model.sync.EmployeeInfoModel
import com.aristopharma.v2.feature.dashboard.data.model.sync.ProductInfoModel

/**
 * Mapper functions to convert between database entities and domain models.
 */

// Employee Info Mappers
fun EmployeeInfoEntity.toModel() = EmployeeInfoModel(
    empId = empId.toIntOrNull(),
    surName = surName,
    empContactNo = contactNo,
    designation = designation,
    department = department
)

fun EmployeeInfoModel.toEntity() = EmployeeInfoEntity(
    empId = empId?.toString() ?: "",
    surName = surName,
    contactNo = empContactNo,
    designation = designation,
    department = department
)

// Product Info Mappers
fun ProductInfoEntity.toModel() = ProductInfoModel(
    productId = productId.toIntOrNull(),
    productCode = productCode,
    brandName = brandName,
    packSize = packSize,
    productGroup = productGroup
)

fun ProductInfoModel.toEntity() = ProductInfoEntity(
    productId = productId?.toString() ?: "",
    productCode = productCode,
    brandName = brandName,
    packSize = packSize,
    productGroup = productGroup
)

// Chemist Info Mappers
fun ChemistInfoEntity.toModel() = ChemistInfoModel(
    chemistId = chemistId,
    name = chemistName,
    address = address,
    specialDiscount = discount?.toString()
)

fun ChemistInfoModel.toEntity() = ChemistInfoEntity(
    chemistId = chemistId ?: "",
    chemistName = name,
    address = address,
    discount = specialDiscount?.toDoubleOrNull()
)

// Menu Permission Mappers
fun MenuPermissionEntity.toModel() = MenuPermission(
    id = id,
    title = title,
    sequence = sequence
)

fun MenuPermission.toEntity() = MenuPermissionEntity(
    id = id ?: 0,
    title = title,
    sequence = sequence
)

// Dashboard Summary Mappers
fun DashboardSummaryEntity.toModel() = DashboardSummary(
    employeeName = employeeName,
    employeeId = employeeId,
    attendanceStatus = attendanceStatus,
    lastSyncTime = lastSyncTime,
    postOrderCount = postOrderCount,
    isFirstSyncDone = isFirstSyncDone
)

fun DashboardSummary.toEntity() = DashboardSummaryEntity(
    id = 1, // Always 1 - single row table
    employeeName = employeeName,
    employeeId = employeeId,
    attendanceStatus = attendanceStatus,
    lastSyncTime = lastSyncTime,
    postOrderCount = postOrderCount,
    isFirstSyncDone = isFirstSyncDone
)
