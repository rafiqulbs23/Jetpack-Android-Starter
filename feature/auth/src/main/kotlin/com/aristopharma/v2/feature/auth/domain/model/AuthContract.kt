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
    val isVisibleByPassOTP : Boolean = false,
    val showForgotPassword : Boolean = true,
)

@Immutable
sealed interface SignInEvent {
    data class UpdateEmpId(val empId: String) : SignInEvent
    data class UpdatePassword(val password: String) : SignInEvent
    data class UpdateConfirmPassword(val confirmPassword: String) : SignInEvent
    data class UpdateOTP(val otp: String) : SignInEvent
    data class UpdateBypassOTP(val enabled: Boolean) : SignInEvent

    data class ValidateAndSignUp(
        val empId: String,
        val password: String,
        val confirmPassword: String,
    ) : SignInEvent
    data class ValidateOTP(val otp: String) : SignInEvent
    data class DeviceLogin(val empId: String, val password: String, val openDashboard: () -> Unit) : SignInEvent
    data class Login(val empId: String, val password: String) : SignInEvent
    data class LoginBypass(val empId: String, val password: String) : SignInEvent
    data object DeleteAllData : SignInEvent
    data object FetchModel : SignInEvent
    data object ShowSignUpUi : SignInEvent
    data object ResetError : SignInEvent
}


