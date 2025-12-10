package com.aristopharma.v2.feature.auth.domain.model

import android.app.Activity
import androidx.compose.runtime.Immutable
import com.aristopharma.v2.core.ui.utils.TextFiledData

@Immutable
data class SignInState(
    val email: TextFiledData = TextFiledData(String()),
    val password: TextFiledData = TextFiledData(String()),
)

@Immutable
sealed interface SignInEvent {
    data class SignInWithSavedCredentials(val activity: Activity) : SignInEvent
    data class SignInWithGoogle(val activity: Activity) : SignInEvent
    data object SignInWithEmailPassword : SignInEvent
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

