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

package com.aristopharma.v2.feature.auth.presentation.screen.signin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aristopharma.v2.core.ui.utils.SnackbarAction
import com.aristopharma.v2.core.ui.utils.StatefulComposable
import com.aristopharma.v2.feature.auth.presentation.component.OtpWaitingDialog
import com.aristopharma.v2.feature.auth.presentation.utils.OtpReceivedListener
import com.aristopharma.v2.feature.auth.presentation.utils.SmsRetrieverHelper
import com.aristopharma.v2.feature.auth.presentation.viewmodel.SignInViewModel
import kotlinx.coroutines.delay

/**
 * Main sign-in screen with OTP-based authentication.
 * Replaces the legacy LoginActivity with Material3 Compose UI.
 */
@Composable
fun SignInScreen(
    onNavigateToDashboard: () -> Unit,
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    signInViewModel: SignInViewModel = hiltViewModel(),
) {
    val signInState by signInViewModel.signInUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // SMS Retriever setup
    val smsRetrieverHelper = remember { SmsRetrieverHelper(context) }
    val otpListener = remember {
        object : OtpReceivedListener {
            override fun onOtpReceived(otp: String) {
                signInViewModel.validateOTP(otp)
            }

            override fun onOtpTimeout() {
                // Handle timeout - could show error message via snackbar
            }
        }
    }

    // Register and unregister SMS receiver
    DisposableEffect(Unit) {
        val intentFilter = smsRetrieverHelper.startSmsRetriever(otpListener)
        val receiver = smsRetrieverHelper.getReceiver()
        
        if (receiver != null && context is android.app.Activity) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                context.registerReceiver(receiver, intentFilter, android.content.Context.RECEIVER_EXPORTED)
            } else {
                @Suppress("DEPRECATION")
                context.registerReceiver(receiver, intentFilter)
            }
        }

        onDispose {
            receiver?.let {
                try {
                    context.unregisterReceiver(it)
                } catch (e: Exception) {
                    // Receiver might already be unregistered
                }
            }
            smsRetrieverHelper.cleanup()
        }
    }

    // Handle navigation to dashboard
    LaunchedEffect(signInState.data.isSignUpSuccessful) {
        if (signInState.data.isSignUpSuccessful) {
            onNavigateToDashboard()
        }
    }

    // Handle SMS waiting dialog
    var showOtpDialog by remember { mutableStateOf(false) }
    signInState.data.isWaitingForSMS.getContentIfNotHandled()?.let {
        if (it) {
            showOtpDialog = true
        }
    }

    StatefulComposable(
        state = signInState,
        onShowSnackbar = onShowSnackbar,
    ) {
        SignInScreenContent(
            state = signInState.data,
            onEmpIdChange = signInViewModel::updateEmpId,
            onPasswordChange = signInViewModel::updatePassword,
            onConfirmPasswordChange = signInViewModel::updateConfirmPassword,
            onOtpChange = signInViewModel::updateOTP,
            onLoginClick = { user, password ->
                if (signInState.data.isBypassOTP) {
                    signInViewModel.loginBypass(user, password)
                } else {
                    signInViewModel.deviceLogin(user, password, onNavigateToDashboard)
                }
            },
            onSignUpClick = {
                signInViewModel.validateAndSignUp(
                    signInState.data.empId.value,
                    signInState.data.password.value,
                    signInState.data.confirmPassword.value,
                )
            },
            onVerifyClick = {
                signInViewModel.validateOTP(signInState.data.otp.value)
            },
            onForgotPasswordClick = {
                signInViewModel.showSignUpUi()
            },
            onBypassToggle = { enabled ->
                signInViewModel.updateBypassOTP(enabled)
            },
        )
    }

    // OTP Waiting Dialog
    if (showOtpDialog) {
        OtpWaitingDialog(
            onDismiss = { showOtpDialog = false },
            onRetry = {
                showOtpDialog = false
                signInViewModel.validateAndSignUp(
                    signInState.data.empId.value,
                    signInState.data.password.value,
                    signInState.data.confirmPassword.value,
                )
            },
        )
    }
}

@Composable
private fun SignInScreenContent(
    state: com.aristopharma.v2.feature.auth.domain.model.SignInState,
    onEmpIdChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onOtpChange: (String) -> Unit,
    onLoginClick: (String, String) -> Unit,
    onSignUpClick: () -> Unit,
    onVerifyClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onBypassToggle: (Boolean) -> Unit,
) {
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var showConfirmPassword by rememberSaveable { mutableStateOf(false) }

    // Animation states
    var logoVisible by remember { mutableStateOf(false) }
    var inputVisible by remember { mutableStateOf(false) }
    val logoAlpha by animateFloatAsState(
        targetValue = if (logoVisible) 1f else 0f,
        animationSpec = tween(800),
        label = "logo_alpha",
    )
    val inputAlpha by animateFloatAsState(
        targetValue = if (inputVisible) 1f else 0f,
        animationSpec = tween(600),
        label = "input_alpha",
    )

    LaunchedEffect(Unit) {
        logoVisible = true
        kotlinx.coroutines.delay(300)
        inputVisible = true
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        val guidelineOffset = maxHeight * 0.35f

        // Bypass OTP Switch (invisible by default, visible in debug builds)
        AnimatedVisibility(
            visible = state.isBypassOTP,
            modifier = Modifier.align(Alignment.TopEnd),
        ) {
            Switch(
                checked = state.isBypassOTP,
                onCheckedChange = onBypassToggle,
                modifier = Modifier.padding(16.dp),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color.Black,
                ),
            )
        }

        // Logo with animation
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = guidelineOffset * 0.3f)
                .alpha(logoAlpha),
        ) {
            // Logo image would go here
            // For now, using a placeholder
            Text(
                text = "Aristopharma",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.Black,
            )
        }

        // Login form
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = guidelineOffset)
                .alpha(inputAlpha),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Header
            Text(
                text = if (state.isVerifyBtnVisible) "Sign Up" else "Login",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Thin),
                fontSize = 24.sp,
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Employee ID field
            OutlinedTextField(
                value = state.empId.value,
                onValueChange = onEmpIdChange,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                },
                label = { Text("Employee ID") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = state.empId.errorMessage != null,
                supportingText = state.empId.errorMessage?.let { { Text(it) } },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                ),
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password field
            OutlinedTextField(
                value = state.password.value,
                onValueChange = onPasswordChange,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                },
                label = { Text("Password") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (showPassword) "Hide password" else "Show password",
                        )
                    }
                },
                isError = state.password.errorMessage != null,
                supportingText = state.password.errorMessage?.let { { Text(it) } },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                ),
            )

            // Confirm Password (visible only during sign up)
            AnimatedVisibility(visible = state.isVerifyBtnVisible) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = state.confirmPassword.value,
                        onValueChange = onConfirmPasswordChange,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                            )
                        },
                        label = { Text("Confirm Password") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                                Icon(
                                    imageVector = if (showConfirmPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (showConfirmPassword) "Hide password" else "Show password",
                                )
                            }
                        },
                        isError = state.confirmPassword.errorMessage != null,
                        supportingText = state.confirmPassword.errorMessage?.let { { Text(it) } },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                        ),
                    )
                }
            }

            // OTP field (visible only during OTP verification)
            AnimatedVisibility(visible = state.isVerifyBtnVisible && !state.isLoginBtnVisible) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = state.otp.value,
                        onValueChange = onOtpChange,
                        label = { Text("OTP") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                        ),
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Login button
            AnimatedVisibility(visible = state.isLoginBtnVisible) {
                Button(
                    onClick = {
                        onLoginClick(state.empId.value, state.password.value)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White,
                    ),
                ) {
                    Text(text = "Login", style = MaterialTheme.typography.labelLarge)
                }
            }

            // Sign Up button
            AnimatedVisibility(visible = state.isSignUpBtnVisible) {
                OutlinedButton(
                    onClick = onSignUpClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(32.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Black,
                    ),
                ) {
                    Text(text = "Sign Up", style = MaterialTheme.typography.labelLarge)
                }
            }

            // Verify button
            AnimatedVisibility(visible = state.isVerifyBtnVisible) {
                Button(
                    onClick = onVerifyClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White,
                    ),
                ) {
                    Text(text = "Verify", style = MaterialTheme.typography.labelLarge)
                }
            }

            // Forgot password
            if (state.isLoginBtnVisible) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Forgot Password?",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp)
                        .clickable { onForgotPasswordClick() },
                    textAlign = TextAlign.End,
                    textDecoration = TextDecoration.None,
                    fontSize = 12.sp,
                )
            }
        }

        // Version text at bottom
        Text(
            text = "Version 1.0.0",
            style = MaterialTheme.typography.labelSmall,
            color = Color.Black.copy(alpha = 0.6f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(8.dp)
                .clickable { /* Show debug info */ },
        )
    }
}
