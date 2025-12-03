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

package com.aristopharma.v2.compose.navigation

import androidx.annotation.StringRes
import com.aristopharma.v2.compose.R
import com.aristopharma.v2.core.navigation.TopLevelDestination as CoreTopLevelDestination

/**
 * Extension to add string resources to TopLevelDestination.
 * This is kept in the app module since it needs access to app string resources.
 */
val CoreTopLevelDestination.iconTextId: Int
    @StringRes get() = when (this) {
        CoreTopLevelDestination.HOME -> R.string.home
        CoreTopLevelDestination.PROFILE -> R.string.profile
    }

val CoreTopLevelDestination.titleTextId: Int
    @StringRes get() = when (this) {
        CoreTopLevelDestination.HOME -> R.string.home
        CoreTopLevelDestination.PROFILE -> R.string.profile
    }

/**
 * Re-export for convenience - use this typealias throughout the app module.
 */
typealias TopLevelDestination = CoreTopLevelDestination
