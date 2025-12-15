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

package com.aristopharma.v2.feature.dashboard.data.model

import kotlinx.serialization.Serializable

/**
 * Data model for dashboard summary information.
 *
 * @param employeeName The employee's name.
 * @param employeeId The employee's ID.
 * @param attendanceStatus The current attendance status (Checked In/Checked Out/Idle).
 * @param lastSyncTime The timestamp of the last sync.
 * @param postOrderCount The count of post orders for today.
 * @param isFirstSyncDone Whether the first sync has been completed.
 */
@Serializable
data class DashboardSummary(
    val employeeName: String = "",
    val employeeId: String = "",
    val attendanceStatus: String = "",
    val lastSyncTime: String = "",
    val postOrderCount: Int = 0,
    val isFirstSyncDone: Boolean = false,
) {
    companion object {
        const val CHECKED_IN = "Checked In"
        const val CHECKED_OUT = "Checked Out"
        const val IDLE = "Idle"
    }

    /**
     * Gets a formatted relative time string for the last sync.
     * Example: "2 hours ago", "Just now"
     */
    fun getLastSyncRelativeTime(): String {
        return if (lastSyncTime.isNotEmpty()) {
            // TODO: Implement relative time formatting
            lastSyncTime
        } else {
            ""
        }
    }

    /**
     * Gets a formatted date string for the last sync.
     * Example: "Jan 15, 2025"
     */
    fun getLastSyncDate(): String {
        return if (lastSyncTime.isNotEmpty()) {
            // TODO: Implement date formatting
            lastSyncTime
        } else {
            "Not synced yet"
        }
    }
}

