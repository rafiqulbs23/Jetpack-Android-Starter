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

package com.aristopharma.v2.feature.dashboard.domain.model

import androidx.compose.runtime.Immutable
import com.aristopharma.v2.core.utils.OneTimeEvent
import com.aristopharma.v2.feature.dashboard.data.model.DashboardMenuItem
import com.aristopharma.v2.feature.dashboard.data.model.DashboardSummary

/**
 * UI state for the dashboard screen.
 *
 * @param menuItems List of menu items to display.
 * @param reportItems List of report menu items to display.
 * @param summary Dashboard summary information.
 * @param isOrderApprovalDotVisible Whether to show red dot on Order History (Manager).
 * @param isSyncSuccess Whether the sync operation was successful.
 */
@Immutable
data class DashboardState(
    val menuItems: List<DashboardMenuItem> = emptyList(),
    val reportItems: List<DashboardMenuItem> = emptyList(),
    val summary: DashboardSummary = DashboardSummary(),
    val isOrderApprovalDotVisible: Boolean = false,
    val isSyncSuccess: OneTimeEvent<Boolean> = OneTimeEvent(false),
)

/**
 * Events for dashboard interactions.
 */
@Immutable
sealed interface DashboardEvent {
    /**
     * Event to trigger first sync.
     */
    data class SyncNow(val empId: String) : DashboardEvent

    /**
     * Event when a menu item is clicked.
     */
    data class MenuItemClick(val menuType: com.aristopharma.v2.feature.dashboard.data.model.DashboardMenuType) : DashboardEvent

    /**
     * Event to fetch menu permissions from database.
     */
    data object FetchMenuPermissions : DashboardEvent

    /**
     * Event to setup dashboard model from preferences.
     */
    data object SetupModel : DashboardEvent

    /**
     * Event to logout from device.
     */
    data object DeviceLogout : DashboardEvent

    /**
     * Event to delete user data.
     */
    data object DeleteUserData : DashboardEvent
}

