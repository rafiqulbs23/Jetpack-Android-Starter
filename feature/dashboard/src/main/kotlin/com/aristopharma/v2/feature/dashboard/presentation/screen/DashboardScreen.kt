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

package com.aristopharma.v2.feature.dashboard.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aristopharma.v2.core.ui.utils.SnackbarAction
import com.aristopharma.v2.core.ui.utils.StatefulComposable
import com.aristopharma.v2.feature.dashboard.data.model.DashboardMenuType
import com.aristopharma.v2.feature.dashboard.domain.model.DashboardEvent
import com.aristopharma.v2.feature.dashboard.presentation.component.DashboardMenuItem
import com.aristopharma.v2.feature.dashboard.presentation.component.DashboardSummaryCard
import com.aristopharma.v2.feature.dashboard.presentation.viewmodel.DashboardViewModel

/**
 * Dashboard screen with Material3 Compose UI.
 * Replaces the legacy DashboardActivity.
 *
 * @param onMenuItemClick Callback when a menu item is clicked.
 * @param onNotificationClick Callback when notification icon is clicked.
 * @param onLogout Callback when user logs out.
 * @param onShowSnackbar Callback to show snackbar messages.
 * @param dashboardViewModel The ViewModel for dashboard operations.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onMenuItemClick: (DashboardMenuType) -> Unit,
    onNotificationClick: () -> Unit,
    onLogout: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
) {
    val uiState by dashboardViewModel.dashboardUiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.data.isSyncSuccess) {
        uiState.data.isSyncSuccess.getContentIfNotHandled()?.let { success ->
            if (success) {
                onShowSnackbar("Sync completed successfully", SnackbarAction.NONE, null)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Dashboard",
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                actions = {
                    IconButton(onClick = onNotificationClick) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        StatefulComposable(
            state = uiState,
            onShowSnackbar = onShowSnackbar,
        ) { state ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) {
                // Summary Card
                DashboardSummaryCard(
                    summary = state.summary,
                    onSyncClick = {
                        val empId = state.summary.employeeId
                        if (empId.isNotEmpty()) {
                            dashboardViewModel.onEvent(DashboardEvent.SyncNow(empId))
                        }
                    },
                    isLoading = uiState.loading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Menu Items Grid
                if (state.menuItems.isNotEmpty()) {
                    Text(
                        text = "Menu",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(state.menuItems) { menuItem ->
                            DashboardMenuItem(
                                menuItem = menuItem,
                                onClick = {
                                    val menuType = enumValues<DashboardMenuType>()
                                        .find { it.name == menuItem.menuItemName }
                                    menuType?.let {
                                        // First notify ViewModel (for feedback on unavailable features)
                                        dashboardViewModel.onEvent(
                                            DashboardEvent.MenuItemClick(it),
                                        )
                                        // Then call navigation callback (for available features)
                                        onMenuItemClick(it)
                                    }
                                },
                            )
                        }
                    }
                }

                // Report Items Grid
                if (state.reportItems.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Reports",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(state.reportItems) { menuItem ->
                            DashboardMenuItem(
                                menuItem = menuItem,
                                onClick = {
                                    val menuType = enumValues<DashboardMenuType>()
                                        .find { it.name == menuItem.menuItemName }
                                    menuType?.let {
                                        dashboardViewModel.onEvent(
                                            DashboardEvent.MenuItemClick(it),
                                        )
                                        onMenuItemClick(it)
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

