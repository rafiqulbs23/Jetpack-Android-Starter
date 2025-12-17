package com.aristopharma.v2.feature.auth.domain.model

import androidx.compose.runtime.Immutable

/**
 * Events for OTP verification screen.
 */
@Immutable
sealed interface OtpEvent {
    /**
     * Event to change OTP button status (enable/disable manual input).
     *
     * @property isEnabled Whether manual OTP input should be enabled.
     */
    data class OnChangeOtpButtonStatus(val isEnabled: Boolean) : OtpEvent
}
