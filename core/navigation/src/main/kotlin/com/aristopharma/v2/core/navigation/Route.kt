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

package com.aristopharma.v2.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlin.reflect.KClass
import kotlinx.serialization.Serializable

// ============================================================================
// Splash Route
// ============================================================================

/**
 * Splash route.
 */
@Serializable
data object Splash

// ============================================================================
// Home Routes
// ============================================================================

/**
 * Home route.
 */
@Serializable
data object Home

/**
 * Home navigation graph route.
 */
@Serializable
data object HomeNavGraph

/**
 * Item route.
 */
@Serializable
data class Item(val itemId: String?)

/**
 * Navigates to the Home navigation graph.
 *
 * @param navOptions Optional navigation options to configure the navigation behavior.
 */
fun NavController.navigateToHomeNavGraph(navOptions: NavOptions? = null) {
    navigate(HomeNavGraph, navOptions)
}

/**
 * Navigates to the Item screen.
 *
 * @param itemId The item ID.
 */
fun NavController.navigateToItemScreen(itemId: String?) {
    navigate(Item(itemId)) { launchSingleTop = true }
}

// ============================================================================
// Auth Routes
// ============================================================================

/**
 * Auth navigation graph route.
 */
@Serializable
data object AuthNavGraph

/**
 * Sign in route.
 */
@Serializable
data object SignIn

/**
 * Navigate to the auth navigation graph.
 *
 * @param navOptions [NavOptions].
 */
fun NavController.navigateToAuthNavGraph(navOptions: NavOptions? = null) {
    navigate(AuthNavGraph, navOptions)
}

/**
 * Navigate to the sign in route.
 *
 * @param navOptions [NavOptions].
 */
fun NavController.navigateToSignInScreen(navOptions: NavOptions? = null) {
    navigate(SignIn, navOptions)
}

// ============================================================================
// Profile Routes
// ============================================================================

/**
 * Serializable data object representing the Profile route.
 */
@Serializable
data object Profile

/**
 * Extension function to navigate to the Profile screen.
 *
 * @param navOptions Options to configure the navigation behavior.
 */
fun NavController.navigateToProfileScreen(navOptions: NavOptions?) {
    navigate(Profile, navOptions)
}

// ============================================================================
// Top Level Destinations
// ============================================================================

/**
 * Enum class representing top-level destinations in a navigation system.
 *
 * @property selectedIcon The selected icon associated with the destination.
 * @property unselectedIcon The unselected icon associated with the destination.
 * @property route The route associated with the destination.
 */
enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: KClass<*>,
) {
    HOME(
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        route = Home::class,
    ),
    PROFILE(
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        route = Profile::class,
    ),
}
