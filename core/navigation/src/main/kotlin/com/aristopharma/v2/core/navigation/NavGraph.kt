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
import androidx.navigation.navigation
import com.aristopharma.v2.core.ui.utils.SnackbarAction
import com.aristopharma.v2.feature.splash.presentation.screens.SplashScreen
import com.aristopharma.v2.feature.auth.presentation.screen.signin.SignInScreen
import com.aristopharma.v2.feature.auth.presentation.screen.signup.SignUpScreen
import com.aristopharma.v2.feature.home.presentation.screen.home.HomeScreen
import com.aristopharma.v2.feature.home.presentation.screen.item.ItemScreen
import com.aristopharma.v2.feature.profile.presentation.screen.ProfileScreen
import kotlin.reflect.KClass

// ============================================================================
// Home Navigation Graph
// ============================================================================

/**
 * Home screen.
 *
 * @param onJetpackClick The click listener for the jetpack.
 * @param onShowSnackbar The snackbar listener.
 */
fun NavGraphBuilder.homeScreen(
    onJetpackClick: (String) -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
) {
    composable<Home> {
        HomeScreen(
            onJetpackClick = onJetpackClick,
            onShowSnackbar = onShowSnackbar,
        )
    }
}

/**
 * Item screen.
 *
 * @param onBackClick The back click listener.
 * @param onShowSnackbar The snackbar listener.
 */
fun NavGraphBuilder.itemScreen(
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
) {
    composable<Item> {
        ItemScreen(
            onBackClick = onBackClick,
            onShowSnackbar = onShowSnackbar,
        )
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
 * Sign in screen.
 *
 * @param onSignUpClick Callback when sign up is clicked.
 * @param onShowSnackbar Callback to show a snackbar.
 */
fun NavGraphBuilder.signInScreen(
    onSignUpClick: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
) {
    composable<SignIn> {
        SignInScreen(
            onSignUpClick = onSignUpClick,
            onShowSnackbar = onShowSnackbar,
        )
    }
}

/**
 * Sign up screen.
 *
 * @param onSignInClick Callback when sign in is clicked.
 * @param onShowSnackbar Callback to show a snackbar.
 */
fun NavGraphBuilder.signUpScreen(
    onSignInClick: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
) {
    composable<SignUp> {
        SignUpScreen(
            onSignInClick = onSignInClick,
            onShowSnackbar = onShowSnackbar,
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
// Profile Navigation Graph
// ============================================================================

/**
 * Splash screen.
 *
 * Navigates to the next route once the view model emits a navigation event.
 */
fun NavGraphBuilder.splashScreen(
    navController: NavHostController,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
) {
    composable<Splash> {
        SplashScreen(
            navController = navController,
            onNavigate = {
                // Navigate to home graph after splash
                navController.navigate(HomeNavGraph) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            },
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
 */
fun NavGraphBuilder.profileScreen(
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
) {
    composable<Profile> {
        ProfileScreen(
            onShowSnackbar = onShowSnackbar,
        )
    }
}

// ============================================================================
// Navigation Host
// ============================================================================

/**
 * Composable function that sets up the navigation host for the application.
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
        )
        authNavGraph(
            nestedNavGraphs = {
                signInScreen(
                    onSignUpClick = { navController.navigateToSignUpScreen() },
                    onShowSnackbar = onShowSnackbar,
                )
                signUpScreen(
                    onSignInClick = { navController.navigateToSignInScreen() },
                    onShowSnackbar = onShowSnackbar,
                )
            },
        )
        homeNavGraph(
            nestedNavGraphs = {
                homeScreen(
                    onJetpackClick = { itemId -> navController.navigateToItemScreen(itemId) },
                    onShowSnackbar = onShowSnackbar,
                )
                itemScreen(
                    onBackClick = navController::popBackStack,
                    onShowSnackbar = onShowSnackbar,
                )
            },
        )
        profileScreen(
            onShowSnackbar = onShowSnackbar,
        )
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
