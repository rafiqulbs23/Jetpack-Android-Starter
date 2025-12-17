package com.aristopharma.v2.feature.auth.domain.model

import androidx.compose.runtime.Immutable

/**
 * UI state for OTP verification screen.
 *
 * @property otp The current OTP value entered by the user.
 * @property isLoading Whether the OTP verification is in progress.
 * @property error Error message if OTP verification failed.
 * @property navigateToHome Whether to navigate to home screen after successful verification.
 * @property isAutoFilledEnable Whether auto-fill from SMS is enabled.
 * @property isOtpButtonEnable Whether manual OTP input is enabled.
 */
@Immutable
data class OtpUiState(
    val otp: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val navigateToHome: Boolean = false,
    val isAutoFilledEnable: Boolean = true,
    val isOtpButtonEnable: Boolean = false
)
