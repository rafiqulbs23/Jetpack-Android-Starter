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

import androidx.datastore.core.DataStore
import com.aristopharma.v2.core.di.IoDispatcher
import com.aristopharma.v2.core.preferences.model.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementation of the [UserPreferencesDataSource] interface using DataStore to manage user preferences.
 *
 * @property datastore The DataStore instance to manage user preferences data.
 * @property ioDispatcher The CoroutineDispatcher for performing I/O operations.
 */
internal class UserPreferencesDataSourceImpl @Inject constructor(
    private val datastore: DataStore<UserDataPreferences>,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : UserPreferencesDataSource {

    /**
     * Retrieves the user profile from the user preferences.
     *
     * @return A [Flow] of [UserDataPreferences].
     */
    override fun getUserDataPreferences(): Flow<UserDataPreferences> =
        datastore.data.flowOn(ioDispatcher)

    /**
     * Retrieves the user ID or throws an exception if the user is not authenticated.
     *
     * @return The user ID as a [String].
     * @throws IllegalStateException if the user is not authenticated.
     */
    override suspend fun getUserIdOrThrow(): String {
        return withContext(ioDispatcher) {
            val userId = datastore.data.first().id
            if (userId.isEmpty()) throw IllegalStateException("User not authenticated")
            userId
        }
    }

    /**
     * Sets the user profile in the user preferences.
     *
     * @param preferencesUserProfile The user [PreferencesUserProfile] to be set.
     */
    override suspend fun setUserProfile(preferencesUserProfile: PreferencesUserProfile) {
        withContext(ioDispatcher) {
            datastore.updateData { userData ->
                userData.copy(
                    id = preferencesUserProfile.id,
                    userName = preferencesUserProfile.userName,
                    profilePictureUriString = preferencesUserProfile.profilePictureUriString,
                    accessToken = preferencesUserProfile.accessToken,
                    refreshToken = preferencesUserProfile.refreshToken,
                    fcmToken = preferencesUserProfile.fcmToken,
                )
            }
        }
    }

    /**
     * Sets the dark theme configuration in the user preferences.
     *
     * @param darkThemeConfigPreferences The dark theme configuration to be set.
     */
    override suspend fun setDarkThemeConfig(darkThemeConfigPreferences: DarkThemeConfigPreferences) {
        withContext(ioDispatcher) {
            datastore.updateData { userData ->
                userData.copy(darkThemeConfigPreferences = darkThemeConfigPreferences)
            }
        }
    }

    /**
     * Sets the dynamic color preferences in the user preferences.
     *
     * @param useDynamicColor A boolean indicating whether dynamic colors should be used.
     */
    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        withContext(ioDispatcher) {
            datastore.updateData { userData ->
                userData.copy(useDynamicColor = useDynamicColor)
            }
        }
    }

    /**
     * Resets the user preferences to their default values.
     */
    override suspend fun resetUserPreferences() {
        withContext(ioDispatcher) {
            datastore.updateData { UserDataPreferences() }
        }
    }
    
    // ========== App-Specific Method Implementations ==========
    
    override suspend fun setEmpId(empId: String) {
        withContext(ioDispatcher) {
            datastore.updateData { it.copy(empId = empId) }
        }
    }
    
    override fun getEmpId(): Flow<String> =
        datastore.data.map { it.empId }.flowOn(ioDispatcher)
    
    override suspend fun setHasPendingOrderApproval(hasPending: Boolean) {
        withContext(ioDispatcher) {
            datastore.updateData { it.copy(hasPendingOrderApproval = hasPending) }
        }
    }
    
    override fun hasPendingOrderApproval(): Flow<Boolean> =
        datastore.data.map { it.hasPendingOrderApproval }.flowOn(ioDispatcher)
    
    override suspend fun setEmployeeInfo(employeeInfo: EmployeeInfoPreferences) {
        withContext(ioDispatcher) {
            datastore.updateData { it.copy(employeeInfo = employeeInfo) }
        }
    }
    
    override fun getEmployeeInfo(): Flow<EmployeeInfoPreferences?> =
        datastore.data.map { it.employeeInfo }.flowOn(ioDispatcher)
    
    override suspend fun updateSyncInfo(syncInfo: SyncInfoPreferences) {
        withContext(ioDispatcher) {
            datastore.updateData { it.copy(syncInfo = syncInfo) }
        }
    }
    
    override fun getSyncInfo(): Flow<SyncInfoPreferences> =
        datastore.data.map { it.syncInfo }.flowOn(ioDispatcher)
    
    override suspend fun setFirstSyncDone(isDone: Boolean) {
        withContext(ioDispatcher) {
            datastore.updateData { 
                it.copy(
                    syncInfo = it.syncInfo.copy(isFirstSyncDone = isDone),
                    dashboardSummary = it.dashboardSummary.copy(isFirstSyncDone = isDone)
                )
            }
        }
    }
    
    override fun isFirstSyncDone(): Flow<Boolean> =
        datastore.data.map { it.syncInfo.isFirstSyncDone }.flowOn(ioDispatcher)
    
    override suspend fun updateAttendanceInfo(attendanceInfo: AttendancePreferences) {
        withContext(ioDispatcher) {
            datastore.updateData { it.copy(attendanceInfo = attendanceInfo) }
        }
    }
    
    override fun getAttendanceInfo(): Flow<AttendancePreferences> =
        datastore.data.map { it.attendanceInfo }.flowOn(ioDispatcher)
    
    override suspend fun updateDashboardSummary(dashboardSummary: DashboardSummaryPreferences) {
        withContext(ioDispatcher) {
            datastore.updateData { it.copy(dashboardSummary = dashboardSummary) }
        }
    }
    
    override fun getDashboardSummary(): Flow<DashboardSummaryPreferences> =
        datastore.data.map { it.dashboardSummary }.flowOn(ioDispatcher)
    
    override suspend fun updateMobileServer(mobileServer: MobileServerPreferences) {
        withContext(ioDispatcher) {
            datastore.updateData { it.copy(mobileServer = mobileServer) }
        }
    }
    
    override fun getMobileServer(): Flow<MobileServerPreferences> =
        datastore.data.map { it.mobileServer }.flowOn(ioDispatcher)
}
