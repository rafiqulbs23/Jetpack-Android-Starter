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

package com.aristopharma.v2.feature.dashboard.presentation.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import com.aristopharma.v2.core.ui.utils.SnackbarAction
import com.aristopharma.v2.feature.dashboard.data.model.DashboardMenuType
import com.aristopharma.v2.feature.dashboard.presentation.screen.DashboardScreen
import com.aristopharma.v2.feature.dashboard.presentation.viewmodel.DashboardViewModel

/**
 * Dashboard route composable for navigation.
 *
 * @param onMenuItemClick Callback when a menu item is clicked.
 * @param onNotificationClick Callback when notification icon is clicked.
 * @param onLogout Callback when user logs out.
 * @param onShowSnackbar Callback to show snackbar messages.
 * @param dashboardViewModel The ViewModel instance (injected via Hilt).
 */
@androidx.compose.runtime.Composable
fun DashboardRoute(
    onMenuItemClick: (DashboardMenuType) -> Unit,
    onNotificationClick: () -> Unit,
    onLogout: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
) {
    DashboardScreen(
        onMenuItemClick = onMenuItemClick,
        onNotificationClick = onNotificationClick,
        onLogout = onLogout,
        onShowSnackbar = onShowSnackbar,
        dashboardViewModel = dashboardViewModel,
    )
}

