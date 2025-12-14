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

package com.aristopharma.v2.feature.auth.presentation.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aristopharma.v2.core.ui.utils.SnackbarAction
import com.aristopharma.v2.feature.auth.presentation.screen.signin.SignInScreen
import com.aristopharma.v2.feature.auth.presentation.viewmodel.SignInViewModel

/**
 * Sign-in route for navigation.
 *
 * @param onNavigateToDashboard Callback when login is successful.
 * @param onShowSnackbar Callback to show snackbar messages.
 * @param signInViewModel The ViewModel instance (injected via Hilt).
 */
@androidx.compose.runtime.Composable
fun SignInRoute(
    onNavigateToDashboard: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    signInViewModel: SignInViewModel = hiltViewModel(),
) {
    // Fetch saved model on screen start
    LaunchedEffect(Unit) {
        signInViewModel.fetchModel()
    }

    SignInScreen(
        onNavigateToDashboard = onNavigateToDashboard,
        onShowSnackbar = onShowSnackbar,
        signInViewModel = signInViewModel,
    )
}

