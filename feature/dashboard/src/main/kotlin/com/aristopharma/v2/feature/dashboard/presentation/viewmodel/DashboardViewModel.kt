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

package com.aristopharma.v2.feature.dashboard.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aristopharma.v2.core.preferences.data.UserPreferencesDataSource
import com.aristopharma.v2.core.ui.utils.UiState
import com.aristopharma.v2.core.ui.utils.updateState
import com.aristopharma.v2.core.utils.OneTimeEvent
import com.aristopharma.v2.feature.dashboard.data.model.DashboardMenuItem
import com.aristopharma.v2.feature.dashboard.data.model.DashboardMenuType
import com.aristopharma.v2.feature.dashboard.data.model.DashboardSummary
import com.aristopharma.v2.feature.dashboard.data.model.MenuPermission
import com.aristopharma.v2.feature.dashboard.domain.model.DashboardEvent
import com.aristopharma.v2.feature.dashboard.domain.model.DashboardState
import com.aristopharma.v2.feature.dashboard.domain.repository.DashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Dashboard feature.
 *
 * Handles dashboard state, menu permissions, sync operations, and navigation.
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository,
    private val userPreferencesDataSource: UserPreferencesDataSource,
) : ViewModel() {

    private val _dashboardUiState = MutableStateFlow(UiState(DashboardState()))
    val dashboardUiState = _dashboardUiState.asStateFlow()

    init {
        setupModel()
        fetchMenuPermissions()
    }

    /**
     * Handles dashboard events.
     */
    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.SyncNow -> syncNow(event.empId)
            is DashboardEvent.MenuItemClick -> handleMenuItemClick(event.menuType)
            is DashboardEvent.FetchMenuPermissions -> fetchMenuPermissions()
            is DashboardEvent.SetupModel -> setupModel()
            is DashboardEvent.DeviceLogout -> deviceLogout()
            is DashboardEvent.DeleteUserData -> deleteUserData()
        }
    }

    /**
     * Sets up the dashboard model from local storage.
     */
    private fun setupModel() {
        viewModelScope.launch {
            try {
                val summary = dashboardRepository.getDashboardSummary()
                if (summary == null || !summary.isFirstSyncDone) {
                    // Try to get user ID, but handle case where user is not authenticated yet
                    val empId = try {
                        userPreferencesDataSource.getUserIdOrThrow()
                    } catch (e: IllegalStateException) {
                        // User not authenticated yet, skip sync for now
                        return@launch
                    }
                    syncNow(empId)
                } else {
                    _dashboardUiState.updateState {
                        copy(summary = summary)
                    }
                }
            } catch (e: Exception) {
                _dashboardUiState.update {
                    it.copy(
                        error = OneTimeEvent(e),
                    )
                }
            }
        }
    }

    /**
     * Fetches menu permissions from the database.
     */
    private fun fetchMenuPermissions() {
        viewModelScope.launch {
            dashboardRepository.getMenuPermissions()
                .catch { error ->
                    _dashboardUiState.update {
                        it.copy(
                            error = OneTimeEvent(error),
                        )
                    }
                }
                .collect { permissions ->
                    val menuItems = mapPermissionsToMenuItems(permissions)
                    _dashboardUiState.updateState {
                        copy(
                            menuItems = menuItems.first,
                            reportItems = menuItems.second,
                        )
                    }
                }
        }
    }

    /**
     * Maps menu permissions to dashboard menu items.
     *
     * @param permissions List of menu permissions.
     * @return Pair of (activity menu items, report menu items).
     */
    private fun mapPermissionsToMenuItems(
        permissions: List<MenuPermission>,
    ): Pair<List<DashboardMenuItem>, List<DashboardMenuItem>> {
        val menuMap = mapOf(
            "Draft Order" to DashboardMenuType.DRAFT_ORDER,
            "Post Order" to DashboardMenuType.POST_ORDER,
            "Post Special Order" to DashboardMenuType.POST_SPECIAL_ORDER,
            "Order History (User)" to DashboardMenuType.ORDER_HISTORY_USER,
            "Order History (Manager)" to DashboardMenuType.ORDER_HISTORY_MANAGER,
            "Leave Management" to DashboardMenuType.LEAVE_MANAGEMENT,
            "Leave" to DashboardMenuType.LEAVE,
            "Live Location" to DashboardMenuType.MANAGER_LIVE_LOCATION,
            "Attendance Report" to DashboardMenuType.ATTENDANCE_REPORT,
            "Attendance" to DashboardMenuType.START_YOUR_DAY,
            "Chemist Sales Report" to DashboardMenuType.CHEMIST_SALES_REPORT,
            "Product Sales Report" to DashboardMenuType.PRODUCT_SALES_REPORT,
            "Sales Summary Report" to DashboardMenuType.SALES_SUMMARY_REPORT,
        )

        val activityItems = mutableListOf<DashboardMenuItem>()
        val reportItems = mutableListOf<DashboardMenuItem>()

        permissions.forEach { permission ->
            val menuType = menuMap[permission.title]
            menuType?.let {
                val menuItem = createMenuItem(it, permission.title)
                when (it) {
                    DashboardMenuType.CHEMIST_SALES_REPORT,
                    DashboardMenuType.PRODUCT_SALES_REPORT,
                    DashboardMenuType.SALES_SUMMARY_REPORT,
                    -> reportItems.add(menuItem)
                    else -> activityItems.add(menuItem)
                }
            }
        }

        return Pair(activityItems, reportItems)
    }

    /**
     * Creates a dashboard menu item from menu type and title.
     *
     * @param menuType The menu type.
     * @param title The menu title.
     * @return The dashboard menu item.
     */
    private fun createMenuItem(
        menuType: DashboardMenuType,
        title: String,
    ): DashboardMenuItem {
        // TODO: Map to actual icons and colors
        return DashboardMenuItem(
            menuItemName = menuType.name,
            displayName = title,
            icon = 0, // TODO: Replace with actual icon
            iconColor = 0, // TODO: Replace with actual color
            isRedDotVisible = menuType == DashboardMenuType.ORDER_HISTORY_MANAGER &&
                _dashboardUiState.value.data.isOrderApprovalDotVisible,
        )
    }

    /**
     * Performs sync operation.
     */
    private fun syncNow(empId: String) {
        viewModelScope.launch {
            _dashboardUiState.update { it.copy(loading = true) }

            dashboardRepository.performFirstSync(empId)
                .fold(
                    onSuccess = {
                        _dashboardUiState.update {
                            it.copy(
                                loading = false,
                                data = it.data.copy(
                                    isSyncSuccess = OneTimeEvent(true),
                                ),
                            )
                        }
                        setupModel()
                    },
                    onFailure = { error ->
                        _dashboardUiState.update {
                            it.copy(
                                loading = false,
                                error = OneTimeEvent(error),
                            )
                        }
                    },
                )
        }
    }

    /**
     * Handles menu item click.
     */
    private fun handleMenuItemClick(menuType: DashboardMenuType) {
        // TODO: Navigate to appropriate screen based on menu type
        // This will be handled by the UI layer
    }

    /**
     * Logs out the user from the device.
     */
    private fun deviceLogout() {
        viewModelScope.launch {
            dashboardRepository.deviceLogout()
        }
    }

    /**
     * Deletes user data.
     */
    private fun deleteUserData() {
        viewModelScope.launch {
            val empId = userPreferencesDataSource.getUserIdOrThrow()
            dashboardRepository.deleteUserData(empId)
                .fold(
                    onSuccess = {
                        _dashboardUiState.update {
                            it.copy(
                                error = OneTimeEvent(
                                    IllegalStateException("User data deleted successfully"),
                                ),
                            )
                        }
                    },
                    onFailure = { error ->
                        _dashboardUiState.update {
                            it.copy(
                                error = OneTimeEvent(error),
                            )
                        }
                    },
                )
        }
    }
}

