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

package com.aristopharma.v2.core.preferences.model

import kotlinx.serialization.Serializable

/**
 * Employee information stored in preferences.
 * Migrated from old Prefs.kt EmployeeInfo model.
 */
@Serializable
data class EmployeeInfoPreferences(
    val empId: Int? = null,
    val surName: String? = null,
    val empContactNo: String? = null,
    val designation: String? = null,
    val department: String? = null
)

/**
 * Sync information stored in preferences.
 * Tracks the status of data synchronization.
 */
@Serializable
data class SyncInfoPreferences(
    val lastSyncTime: String = "",
    val employeeCount: Int = 0,
    val productCount: Int = 0,
    val chemistCount: Int = 0,
    val isFirstSyncDone: Boolean = false
)

/**
 * Attendance status stored in preferences.
 * Tracks current attendance state.
 */
@Serializable
data class AttendancePreferences(
    val status: String = "IDLE", // IDLE, CHECKED_IN, CHECKED_OUT
    val checkInTime: String = "",
    val checkOutTime: String = "",
    val date: String = ""
)

/**
 * Dashboard summary information stored in preferences.
 * Quick access to dashboard metrics.
 */
@Serializable
data class DashboardSummaryPreferences(
    val employeeName: String = "",
    val employeeId: String = "",
    val attendanceStatus: String = "",
    val lastSyncTime: String = "",
    val postOrderCount: Int = 0,
    val isFirstSyncDone: Boolean = false
)

/**
 * Mobile server configuration stored in preferences.
 */
@Serializable
data class MobileServerPreferences(
    val serverUrl: String = "",
    val apiKey: String = "",
    val isConfigured: Boolean = false
)
