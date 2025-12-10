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

package com.aristopharma.v2.feature.auth.presentation.viewmodel.signin

import android.app.Activity
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.aristopharma.v2.core.extensions.isEmailValid
import com.aristopharma.v2.core.extensions.isPasswordValid
import com.aristopharma.v2.core.ui.utils.UiState
import com.aristopharma.v2.core.ui.utils.updateState
import com.aristopharma.v2.core.ui.utils.updateWith
import com.aristopharma.v2.feature.auth.domain.usecase.SignInWithEmailAndPasswordUseCase
import com.aristopharma.v2.feature.auth.domain.usecase.SignInWithGoogleUseCase
import com.aristopharma.v2.feature.auth.domain.usecase.SignInWithSavedCredentialsUseCase
import com.aristopharma.v2.feature.auth.domain.model.SignInState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * [ViewModel] for [SignInScreen].
 *
 * @param authRepository [AuthRepository].
 */
@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInWithSavedCredentials: SignInWithSavedCredentialsUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val signInWithEmailAndPasswordUseCase: SignInWithEmailAndPasswordUseCase,
) : ViewModel() {
    private val _signInUiState = MutableStateFlow(UiState(SignInState()))
    val signInUiState = _signInUiState.asStateFlow()

    fun updateEmail(email: String) {
        _signInUiState.updateState {
            copy(
                email = emailField(email),
            )
        }
    }

    fun updatePassword(password: String) {
        _signInUiState.updateState {
            copy(
                password = passwordField(password),
            )
        }
    }

    fun signInWithSavedCredentials(activity: Activity) {
       /* _signInUiState.updateWith {
            signInWithSavedCredentials(activity)
        }*/
    }

    fun signInWithGoogle(activity: Activity) {
        _signInUiState.updateWith { signInWithGoogleUseCase(activity) }
    }

    fun loginWithEmailAndPassword() {
        val currentState = _signInUiState.value.data
        _signInUiState.updateWith {
            signInWithEmailAndPasswordUseCase(
                email = currentState.email.value,
                password = currentState.password.value,
            )
        }
    }
}

private fun emailField(value: String) = com.aristopharma.v2.core.ui.utils.TextFiledData(
    value = value,
    errorMessage = if (value.isEmailValid()) null else "Email Not Valid",
)

private fun passwordField(value: String) = com.aristopharma.v2.core.ui.utils.TextFiledData(
    value = value,
    errorMessage = if (value.isPasswordValid()) null else "Password Not Valid",
)
