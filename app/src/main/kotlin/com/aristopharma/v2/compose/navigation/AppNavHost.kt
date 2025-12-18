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

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.aristopharma.v2.core.navigation.*
import com.aristopharma.v2.core.ui.utils.SnackbarAction
import com.aristopharma.v2.feature.auth.presentation.screen.OtpScreen
import com.aristopharma.v2.feature.auth.presentation.screen.signin.SignInScreen
import com.aristopharma.v2.feature.dashboard.presentation.screen.DashboardScreen
import com.aristopharma.v2.feature.home.presentation.screen.home.HomeScreen
import com.aristopharma.v2.feature.home.presentation.screen.item.ItemScreen
import com.aristopharma.v2.feature.profile.presentation.screen.ProfileScreen
import com.aristopharma.v2.feature.splash.presentation.screens.SplashScreen
import kotlin.reflect.KClass

// ============================================================================
// Navigation Host
// ============================================================================

/**
 * Composable function that sets up the navigation host for the application.
 *
 * This function wires all feature screens into the navigation graph using
 * the callback pattern defined in core/navigation.
 *
 * @param navController The navigation controller for managing navigation.
 * @param startDestination The starting destination based on user login status.
 * @param onShowSnackbar A lambda function to show a snackbar with a message and an action.
 * @param modifier The modifier to be applied to the NavHost.
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: KClass<*>,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        splashScreen(
            navController = navController,
            onShowSnackbar = onShowSnackbar,
        ) { onNavigateToDashboard, onNavigateToSignIn ->
            SplashScreen(
                onNavigateToDashboard = onNavigateToDashboard,
                onNavigateToSignIn = onNavigateToSignIn,
            )
        }
        authNavGraph(
            nestedNavGraphs = {
                signInScreen(navController, onShowSnackbar) { nav, onNavigateToDashboard, snackbar ->
                    SignInScreen(
                        navController = nav,
                        onNavigateToDashboard = onNavigateToDashboard,
                        onShowSnackbar = snackbar,
                        navigatrTorOtp = { username, password ->
                            navController.navigate(
                                OtpVerification(
                                    username = username,
                                    password = password,
                                )
                            )
                        }
                    )
                }
                otpVerificationScreen(navController) { nav, username, password, onLoginSuccess ->
                    OtpScreen(
                        username = username,
                        password = password,
                        navController = nav,
                        onLoginSuccess = onLoginSuccess
                    )
                }
            },
        )
        dashboardScreen(navController, onShowSnackbar) { nav, onMenuItemClick, onNotificationClick, snackbar ->
            DashboardScreen(
                onMenuItemClick = { menuType ->
                  //  onMenuItemClick(menuType)
                },
                onNotificationClick = {
                    // TODO: Navigate to notifications
                },
                onLogout = {
                    // Navigate to login
                    val navOptions = navOptions {
                        popUpTo(nav.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                    nav.navigate(SignIn, navOptions)
                },
                onShowSnackbar = snackbar
            )
        }
        homeNavGraph(
            nestedNavGraphs = {
                homeScreen(
                    onJetpackClick = { itemId -> navController.navigateToItemScreen(itemId) },
                    onShowSnackbar = onShowSnackbar,
                ) { onJetpackClick, snackbar ->
                    HomeScreen(
                        onJetpackClick = onJetpackClick,
                        onShowSnackbar = snackbar,
                    )
                }
                itemScreen(
                    onBackClick = navController::popBackStack,
                    onShowSnackbar = onShowSnackbar,
                ) { onBackClick, snackbar ->
                    ItemScreen(
                        onBackClick = onBackClick,
                        onShowSnackbar = snackbar,
                    )
                }
            },
        )
        profileScreen(
            onShowSnackbar = onShowSnackbar,
        ) { snackbar ->
            ProfileScreen(
                onShowSnackbar = snackbar,
            )
        }
    }
}

/**
 * Composable function that sets up the navigation host with automatic start destination selection.
 *
 * @param navController The navigation controller for managing navigation.
 * @param isUserLoggedIn Whether the user is logged in.
 * @param onShowSnackbar A lambda function to show a snackbar with a message and an action.
 * @param modifier The modifier to be applied to the NavHost.
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    isUserLoggedIn: Boolean,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    modifier: Modifier = Modifier,
) {
    // Splash should be the first route
    val startDestination = Splash::class
    
    AppNavHost(
        navController = navController,
        startDestination = startDestination,
        onShowSnackbar = onShowSnackbar,
        modifier = modifier,
    )
}
