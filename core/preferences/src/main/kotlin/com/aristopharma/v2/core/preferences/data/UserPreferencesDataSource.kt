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

package com.aristopharma.v2.core.preferences.data

import com.aristopharma.v2.core.preferences.model.*
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining methods to interact with user preferences data source.
 */
interface UserPreferencesDataSource {

    /**
     * Retrieves the user profile from the user preferences.
     *
     * @return A [Flow] of [UserDataPreferences].
     */
     fun getUserDataPreferences(): Flow<UserDataPreferences>

    /**
     * Retrieves the user ID or throws an exception if the user is not authenticated.
     *
     * @return The user ID as a [String].
     * @throws IllegalStateException if the user is not authenticated.
     */
    suspend fun getUserIdOrThrow(): String

    /**
     * Sets the user profile in the user preferences.
     *
     * @param preferencesUserProfile The user ID to be set.
     */
    suspend fun setUserProfile(preferencesUserProfile: PreferencesUserProfile)

    /**
     * Sets the dark theme configuration in the user preferences.
     *
     * @param darkThemeConfigPreferences The dark theme configuration to be set.
     */
    suspend fun setDarkThemeConfig(darkThemeConfigPreferences: DarkThemeConfigPreferences)

    /**
     * Sets the dynamic color preferences in the user preferences.
     *
     * @param useDynamicColor A boolean indicating whether dynamic colors should be used.
     */
    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)

    /**
     * Resets the user preferences to their default values.
     */
    suspend fun resetUserPreferences()
    
    // ========== App-Specific Methods (migrated from old Prefs.kt) ==========
    
    /**
     * Sets the employee ID.
     */
    suspend fun setEmpId(empId: String)
    
    /**
     * Gets the employee ID as a Flow.
     */
    fun getEmpId(): Flow<String>
    
    /**
     * Sets the pending order approval flag.
     */
    suspend fun setHasPendingOrderApproval(hasPending: Boolean)
    
    /**
     * Gets the pending order approval flag as a Flow.
     */
    fun hasPendingOrderApproval(): Flow<Boolean>
    
    /**
     * Sets employee information.
     */
    suspend fun setEmployeeInfo(employeeInfo: EmployeeInfoPreferences)
    
    /**
     * Gets employee information as a Flow.
     */
    fun getEmployeeInfo(): Flow<EmployeeInfoPreferences?>
    
    /**
     * Updates sync information.
     */
    suspend fun updateSyncInfo(syncInfo: SyncInfoPreferences)
    
    /**
     * Gets sync information as a Flow.
     */
    fun getSyncInfo(): Flow<SyncInfoPreferences>
    
    /**
     * Sets first sync done flag.
     */
    suspend fun setFirstSyncDone(isDone: Boolean)
    
    /**
     * Checks if first sync is done.
     */
    fun isFirstSyncDone(): Flow<Boolean>
    
    /**
     * Updates attendance information.
     */
    suspend fun updateAttendanceInfo(attendanceInfo: AttendancePreferences)
    
    /**
     * Gets attendance information as a Flow.
     */
    fun getAttendanceInfo(): Flow<AttendancePreferences>
    
    /**
     * Updates dashboard summary.
     */
    suspend fun updateDashboardSummary(dashboardSummary: DashboardSummaryPreferences)
    
    /**
     * Gets dashboard summary as a Flow.
     */
    fun getDashboardSummary(): Flow<DashboardSummaryPreferences>
    
    /**
     * Updates mobile server configuration.
     */
    suspend fun updateMobileServer(mobileServer: MobileServerPreferences)
    
    /**
     * Gets mobile server configuration as a Flow.
     */
    fun getMobileServer(): Flow<MobileServerPreferences>
}
