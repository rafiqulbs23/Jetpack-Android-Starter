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

package com.aristopharma.v2.feature.auth.presentation.viewmodel.signup

import android.app.Activity
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.aristopharma.v2.core.extensions.isEmailValid
import com.aristopharma.v2.core.extensions.isPasswordValid
import com.aristopharma.v2.core.extensions.isValidFullName
import com.aristopharma.v2.core.ui.utils.UiState
import com.aristopharma.v2.core.ui.utils.updateState
import com.aristopharma.v2.core.ui.utils.updateWith
import com.aristopharma.v2.feature.auth.domain.usecase.RegisterWithEmailAndPasswordUseCase
import com.aristopharma.v2.feature.auth.domain.usecase.RegisterWithGoogleUseCase
import com.aristopharma.v2.feature.auth.domain.model.SignUpState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * [ViewModel] for [SignUpScreen].
 *
 * @param authRepository [AuthRepository].
 */
@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val registerWithGoogleUseCase: RegisterWithGoogleUseCase,
    private val registerWithEmailAndPasswordUseCase: RegisterWithEmailAndPasswordUseCase,
) : ViewModel() {
    private val _signUpUiState = MutableStateFlow(UiState(SignUpState()))
    val signUpUiState = _signUpUiState.asStateFlow()

    fun updateName(name: String) {
        _signUpUiState.updateState {
            copy(
                name = nameField(name),
            )
        }
    }

    fun updateEmail(email: String) {
        _signUpUiState.updateState {
            copy(
                email = emailField(email),
            )
        }
    }

    fun updatePassword(password: String) {
        _signUpUiState.updateState {
            copy(
                password = passwordField(password),
            )
        }
    }

    fun registerWithGoogle(activity: Activity) {
        _signUpUiState.updateWith { registerWithGoogleUseCase(activity) }
    }

    fun registerWithEmailAndPassword(activity: Activity) {
        val currentState = _signUpUiState.value.data
        _signUpUiState.updateWith {
            registerWithEmailAndPasswordUseCase(
                name = currentState.name.value,
                email = currentState.email.value,
                password = currentState.password.value,
                activity = activity,
            )
        }
    }
}

private fun nameField(value: String) = com.aristopharma.v2.core.ui.utils.TextFiledData(
    value = value,
    errorMessage = if (value.isValidFullName()) null else "Name Not Valid",
)

private fun emailField(value: String) = com.aristopharma.v2.core.ui.utils.TextFiledData(
    value = value,
    errorMessage = if (value.isEmailValid()) null else "Email Not Valid",
)

private fun passwordField(value: String) = com.aristopharma.v2.core.ui.utils.TextFiledData(
    value = value,
    errorMessage = if (value.isPasswordValid()) null else "Password Not Valid",
)
