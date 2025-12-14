package com.aristopharma.v2.feature.auth.domain.model

import android.app.Activity
import androidx.compose.runtime.Immutable
import com.aristopharma.v2.core.ui.utils.TextFiledData
import com.aristopharma.v2.core.utils.OneTimeEvent

@Immutable
data class SignInState(
    val empId: TextFiledData = TextFiledData(String()),
    val password: TextFiledData = TextFiledData(String()),
    val confirmPassword: TextFiledData = TextFiledData(String()),
    val otp: TextFiledData = TextFiledData(String()),
    val isLoginBtnVisible: Boolean = true,
    val isSignUpBtnVisible: Boolean = true,
    val isVerifyBtnVisible: Boolean = false,
    val isSignUpSuccessful: Boolean = false,
    val isWaitingForSMS: OneTimeEvent<Boolean> = OneTimeEvent(false),
    val isBypassOTP: Boolean = false,
)

@Immutable
sealed interface SignInEvent {
    data object ValidateAndSignUp : SignInEvent
    data class ValidateOTP(val otp: String) : SignInEvent
    data class DeviceLogin(val openDashboard: () -> Unit) : SignInEvent
    data class LoginBypass(val empId: String, val password: String) : SignInEvent
    data object DeleteAllData : SignInEvent
    data object FetchModel : SignInEvent
}

@Immutable
data class SignUpState(
    val name: TextFiledData = TextFiledData(String()),
    val email: TextFiledData = TextFiledData(String()),
    val password: TextFiledData = TextFiledData(String()),
)

@Immutable
sealed interface SignUpEvent {
    data class RegisterWithGoogle(val activity: Activity) : SignUpEvent
    data class RegisterWithEmailPassword(val activity: Activity) : SignUpEvent
}


