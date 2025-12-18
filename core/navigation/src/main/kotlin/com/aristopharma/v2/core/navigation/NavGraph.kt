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

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.aristopharma.v2.core.ui.utils.SnackbarAction

// NO direct screen imports - screens are provided via callbacks from app module
import kotlin.reflect.KClass

// ============================================================================
// Home Navigation Graph
// ============================================================================

/**
 * Home screen.
 *
 * @param onJetpackClick The click listener for the jetpack.
 * @param onShowSnackbar The snackbar listener.
 * @param content The composable content for the home screen (provided by app module).
 */
fun NavGraphBuilder.homeScreen(
    onJetpackClick: (String) -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    content: @Composable ((String) -> Unit, suspend (String, SnackbarAction, Throwable?) -> Boolean) -> Unit,
) {
    composable<Home> {
        content(onJetpackClick, onShowSnackbar)
    }
}

/**
 * Item screen.
 *
 * @param onBackClick The back click listener.
 * @param onShowSnackbar The snackbar listener.
 * @param content The composable content for the item screen (provided by app module).
 */
fun NavGraphBuilder.itemScreen(
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    content: @Composable (() -> Unit, suspend (String, SnackbarAction, Throwable?) -> Boolean) -> Unit,
) {
    composable<Item> {
        content(onBackClick, onShowSnackbar)
    }
}

/**
 * Home navigation graph.
 *
 * @param nestedNavGraphs The nested navigation graphs.
 */
fun NavGraphBuilder.homeNavGraph(
    nestedNavGraphs: NavGraphBuilder.() -> Unit,
) {
    navigation<HomeNavGraph>(startDestination = Home) {
        nestedNavGraphs()
    }
}

// ============================================================================
// Auth Navigation Graph
// ============================================================================

/**
 * Sign in screen route extension for NavGraphBuilder.
 *
 * @param navController The navigation controller for managing navigation.
 * @param onShowSnackbar Callback to show a snackbar.
 * @param content The composable content for the sign-in screen (provided by app module).
 */
fun NavGraphBuilder.signInScreen(
    navController: NavHostController,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,

    content: @Composable (NavHostController, () -> Unit, suspend (String, SnackbarAction, Throwable?) -> Boolean) -> Unit,
) {
    composable<SignIn> {
        content(
            navController,
            {
                // onNavigateToDashboard
                val navOptions = navOptions {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    launchSingleTop = true
                }
                navController.navigate(Dashboard, navOptions)
            },
            onShowSnackbar,

        )
    }
}

/**
 * OTP verification screen route extension for NavGraphBuilder.
 *
 * @param navController The navigation controller for managing navigation.
 * @param content The composable content for the OTP screen (provided by app module).
 */
fun NavGraphBuilder.otpVerificationScreen(
    navController: NavHostController,
    content: @Composable (NavHostController, String, String, () -> Unit) -> Unit,
) {
    composable<OtpVerification> { backStackEntry ->
        val otpVerification = backStackEntry.toRoute<OtpVerification>()
        
        content(
            navController,
            otpVerification.username,
            otpVerification.password,
            {
                // onLoginSuccess
                val navOptions = navOptions {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    launchSingleTop = true
                }
                navController.navigate(Dashboard, navOptions)
            }
        )
    }
}

/**
 * Auth navigation graph.
 *
 * @param nestedNavGraphs Nested navigation graphs.
 */
fun NavGraphBuilder.authNavGraph(
    nestedNavGraphs: NavGraphBuilder.() -> Unit,
) {
    navigation<AuthNavGraph>(startDestination = SignIn) {
        nestedNavGraphs()
    }
}

// ============================================================================
// Dashboard Navigation
// ============================================================================

/**
 * Dashboard screen route extension for NavGraphBuilder.
 *
 * @param navController The navigation controller for managing navigation.
 * @param onShowSnackbar Callback to show a snackbar.
 * @param content The composable content for the dashboard screen (provided by app module).
 */
fun NavGraphBuilder.dashboardScreen(
    navController: NavHostController,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    content: @Composable (NavHostController, () -> Unit, () -> Unit, suspend (String, SnackbarAction, Throwable?) -> Boolean) -> Unit,
) {
    composable<Dashboard> {
        content(
            navController,
            {
                // onMenuItemClick - navigate to respective screens
                // TODO: Implement menu item navigation
            },
            {
                // onNotificationClick
                // TODO: Navigate to notifications
            },
            onShowSnackbar
        )
    }
}

// ============================================================================
// Splash Screen
// ============================================================================



/**
 * Splash screen.
 *
 * @param navController The navigation controller.
 * @param onShowSnackbar Callback to show snackbar.
 * @param content The composable content for splash screen (provided by app module).
 */
fun NavGraphBuilder.splashScreen(
    navController: NavHostController,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    content: @Composable (() -> Unit, () -> Unit) -> Unit,
) {
    composable<Splash> {
        content(
            {
                // onNavigateToDashboard
                navController.navigate(Dashboard) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    launchSingleTop = true
                }
            },
            {
                // onNavigateToSignIn
                navController.navigate(SignIn) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
    }
}

// ============================================================================
// Profile Navigation Graph
// ============================================================================

/**
 * Adds the Profile screen to the navigation graph.
 *
 * @param onShowSnackbar Lambda function to show a snackbar message.
 * @param content The composable content for the profile screen (provided by app module).
 */
fun NavGraphBuilder.profileScreen(
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    content: @Composable (suspend (String, SnackbarAction, Throwable?) -> Boolean) -> Unit,
) {
    composable<Profile> {
        content(onShowSnackbar)
    }
}

// ============================================================================
// Navigation Host
// ============================================================================

// AppNavHost has been moved to app module
// See: app/src/main/kotlin/com/aristopharma/v2/compose/navigation/AppNavHost.kt
//
// This allows the app module to wire all feature screens without creating
// circular dependencies between core/navigation and feature modules.
