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
 * Represents user data saved in DataStore Preferences.
 *
 * This data class is used to store information about a user, including their ID, name, profile picture URI string,
 * preferred theme brand, dark theme configuration, dynamic color preference, and app-specific data.
 *
 * @property id The unique identifier for the user. Defaults to empty if not provided.
 * @property userName The name of the user. Defaults to null if not provided.
 * @property profilePictureUriString The URI string for the user's profile picture, if available. Defaults to `null` if not provided.
 * @property accessToken The user's access token for API authentication.
 * @property refreshToken The user's refresh token for renewing access.
 * @property fcmToken The Firebase Cloud Messaging token for push notifications.
 * @property darkThemeConfigPreferences The user's preferred dark theme configuration. Defaults to [DarkThemeConfigPreferences.FOLLOW_SYSTEM].
 * @property useDynamicColor A boolean indicating whether the user prefers dynamic colors. Defaults to `true`.
 * 
 * App-Specific Fields (migrated from old Prefs.kt):
 * @property empId The employee ID.
 * @property hasPendingOrderApproval Flag indicating if there are pending orders requiring approval.
 * @property employeeInfo Employee information.
 * @property syncInfo Synchronization status and metadata.
 * @property attendanceInfo Current attendance status.
 * @property dashboardSummary Dashboard summary metrics.
 * @property mobileServer Mobile server configuration.
 */
@Serializable
data class UserDataPreferences(
    // User Authentication & Profile
    val id: String = String(),
    val userName: String? = null,
    val profilePictureUriString: String? = null,
    val accessToken: String = "",
    val refreshToken: String = "",
    val fcmToken: String = "",
    
    // UI Preferences
    val darkThemeConfigPreferences: DarkThemeConfigPreferences = DarkThemeConfigPreferences.FOLLOW_SYSTEM,
    val useDynamicColor: Boolean = true,
    
    // App-Specific Data (migrated from old Prefs.kt)
    val empId: String = "",
    val hasPendingOrderApproval: Boolean = false,
    val employeeInfo: EmployeeInfoPreferences? = null,
    val syncInfo: SyncInfoPreferences = SyncInfoPreferences(),
    val attendanceInfo: AttendancePreferences = AttendancePreferences(),
    val dashboardSummary: DashboardSummaryPreferences = DashboardSummaryPreferences(),
    val mobileServer: MobileServerPreferences = MobileServerPreferences(),
)
