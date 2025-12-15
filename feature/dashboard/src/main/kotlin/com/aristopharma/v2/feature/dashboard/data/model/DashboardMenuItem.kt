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

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Data model for dashboard menu items.
 *
 * @param menuItemName The name/identifier of the menu item (e.g., "POST_ORDER").
 * @param displayName The human-readable display name.
 * @param icon The icon resource ID or vector.
 * @param iconColor The color resource ID for the icon.
 * @param isRedDotVisible Whether to show a red dot indicator (e.g., for pending approvals).
 */
data class DashboardMenuItem(
    val menuItemName: String = "",
    val displayName: String = "",
    val icon: Any, // Can be ImageVector or Int (drawable resource)
    val iconColor: Int,
    val isRedDotVisible: Boolean = false,
) {
    /**
     * Gets the formatted display name, using displayName if available, otherwise formatting menuItemName.
     */
    val formattedDisplayName: String
        get() = if (displayName.isNotEmpty()) {
            displayName
        } else {
            menuItemName.replace("_", " ")
                .lowercase()
                .replaceFirstChar { it.uppercase() }
        }
}

