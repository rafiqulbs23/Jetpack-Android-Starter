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

package com.aristopharma.v2.feature.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aristopharma.v2.core.ui.utils.UiState
import com.aristopharma.v2.core.ui.utils.updateState
import com.aristopharma.v2.core.utils.OneTimeEvent
import com.aristopharma.v2.feature.auth.data.model.LoginModel
import com.aristopharma.v2.feature.auth.data.model.LoginPostModel
import com.aristopharma.v2.feature.auth.data.model.OTPValidationRequest
import com.aristopharma.v2.feature.auth.domain.model.SignInState
import com.aristopharma.v2.feature.auth.BuildConfig
import com.aristopharma.v2.feature.auth.domain.repository.AuthRepository
import com.aristopharma.v2.feature.notification.data.utils.FcmTokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import com.aristopharma.v2.core.ui.utils.TextFiledData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for OTP-based authentication.
 *
 * Handles login/signup flow with employee ID, password, and OTP validation.
 */
@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val fcmTokenManager: FcmTokenManager,
) : ViewModel() {

    private val _signInUiState = MutableStateFlow(
        UiState(
            SignInState(
                isBypassOTP = BuildConfig.ENABLE_BYPASS_OTP,
            ),
        ),
    )
    val signInUiState = _signInUiState.asStateFlow()

    private var loginModel: LoginModel = LoginModel()

    /**
     * Fetches saved login model and initializes UI state.
     */
    fun fetchModel() {
        viewModelScope.launch {
            val savedModel = authRepository.getLoginModel()
            loginModel = savedModel ?: LoginModel()
            
            _signInUiState.updateState {
                copy(
                    empId = TextFiledData(loginModel.empId),
                    password = TextFiledData(loginModel.password),
                )
            }
        }
    }

    /**
     * Updates employee ID in the state.
     */
    fun updateEmpId(empId: String) {
        _signInUiState.updateState {
            copy(empId = TextFiledData(empId))
        }
    }

    /**
     * Updates password in the state.
     */
    fun updatePassword(password: String) {
        _signInUiState.updateState {
            copy(password = TextFiledData(password))
        }
    }

    /**
     * Updates confirm password in the state.
     */
    fun updateConfirmPassword(confirmPassword: String) {
        _signInUiState.updateState {
            copy(confirmPassword = TextFiledData(confirmPassword))
        }
    }

    /**
     * Updates OTP in the state.
     */
    fun updateOTP(otp: String) {
        _signInUiState.updateState {
            copy(otp = TextFiledData(otp))
        }
    }

    /**
     * Validates input and initiates sign up process.
     */
    fun validateAndSignUp(user: String, password: String, confirmPassword: String) {
        val validation = validateInput(user, password, confirmPassword)
        if (validation != null) {
            _signInUiState.update {
                it.copy(
                    error = OneTimeEvent(IllegalArgumentException(validation)),
                )
            }
            return
        }

        loginModel = loginModel.copy(
            empId = user,
            password = password,
        )

        generateFCMTokenAndSignUp()
    }

    /**
     * Validates user input for sign up.
     */
    private fun validateInput(user: String, password: String, confirmPassword: String): String? {
        return when {
            user.isEmpty() -> "User cannot be empty"
            password.isEmpty() || confirmPassword.isEmpty() -> "Password cannot be empty"
            password.length < 4 -> "Password must be at least 4 digits"
            password != confirmPassword -> "Passwords don't match"
            else -> null
        }
    }

    /**
     * Generates FCM token and initiates sign up.
     */
    private fun generateFCMTokenAndSignUp() {
        viewModelScope.launch {
            _signInUiState.update { it.copy(loading = true) }

            val isBypassOTP = _signInUiState.value.data.isBypassOTP
            val fcmToken = if (isBypassOTP) {
                // Dummy FCM token for bypass mode
                "dHUS-WkLTguhZN9DyYiNAc:APA91bEhZ4CxcswbCattBu5LFYML5UyEmQi1J-870R8TwLMiXHOTedwIdJg0grDN815m3jbOVF0Mb0JzcaE7CDUOf9ttuV8RFWq9lOVdJcxYOayICf3v2wWgyKEe6oj3PN8_m4AyJT7X"
            } else {
                getFCMToken()
            }

            if (fcmToken == null) {
                _signInUiState.update {
                    it.copy(
                        loading = false,
                        error = OneTimeEvent(Exception("Failed to get FCM token")),
                    )
                }
                return@launch
            }

            loginModel = loginModel.copy(fcmToken = fcmToken)
            signUp(empId = loginModel.empId, fcmToken = fcmToken)
        }
    }

    /**
     * Retrieves FCM token using FcmTokenManager from notification feature.
     */
    private suspend fun getFCMToken(): String? {
        return fcmTokenManager.getFCMToken()
    }

    /**
     * Performs sign up/login with employee ID and FCM token.
     */
    private fun signUp(empId: String, fcmToken: String) {
        viewModelScope.launch {
            val result = authRepository.login(LoginPostModel(userName = empId, fcmToken = fcmToken))

            result.fold(
                onSuccess = { response ->
                    loginModel = loginModel.copy(
                        empId = empId,
                        accessToken = response.token,
                        refreshToken = response.refreshToken,
                        otp = response.otpCode?.toString() ?: "",
                    )

                    authRepository.saveLoginModel(loginModel)

                    _signInUiState.update {
                        it.copy(
                            loading = false,
                            data = it.data.copy(isWaitingForSMS = OneTimeEvent(true)),
                        )
                    }
                },
                onFailure = { error ->
                    _signInUiState.update {
                        it.copy(
                            loading = false,
                            error = OneTimeEvent(error),
                        )
                    }
                },
            )
        }
    }

    /**
     * Validates OTP code.
     */
    fun validateOTP(otp: String) {
        viewModelScope.launch {
            _signInUiState.update { it.copy(loading = true) }

            val empIdInt = loginModel.empId.toIntOrNull()
            val otpInt = otp.toIntOrNull()

            if (empIdInt == null || otpInt == null) {
                _signInUiState.update {
                    it.copy(
                        loading = false,
                        error = OneTimeEvent(IllegalArgumentException("Invalid employee ID or OTP")),
                    )
                }
                return@launch
            }

            val result = authRepository.validateOTP(OTPValidationRequest(emp_Id = empIdInt, otp = otpInt))

            result.fold(
                onSuccess = { response ->
                    if (response.is_Validated) {
                        loginModel = loginModel.copy(
                            isLoggedIn = true,
                            isSignedUp = true,
                        )
                        authRepository.saveLoginModel(loginModel)

                        _signInUiState.update {
                            it.copy(
                                loading = false,
                                data = it.data.copy(isSignUpSuccessful = true),
                            )
                        }
                    } else {
                        _signInUiState.update {
                            it.copy(
                                loading = false,
                                error = OneTimeEvent(Exception("OTP validation failed")),
                            )
                        }
                    }
                },
                onFailure = { error ->
                    _signInUiState.update {
                        it.copy(
                            loading = false,
                            error = OneTimeEvent(error),
                        )
                    }
                },
            )
        }
    }

    /**
     * Login bypass for testing purposes.
     */
    fun loginBypass(empId: String, password: String) {
        if (empId.isEmpty() || password.isEmpty()) {
            _signInUiState.update {
                it.copy(
                    error = OneTimeEvent(IllegalArgumentException("Employee ID or password is empty")),
                )
            }
            return
        }

        loginModel = loginModel.copy(
            empId = empId,
            password = password,
        )

        val dummyFCM = "dHUS-WkLTguhZN9DyYiNAc:APA91bEhZ4CxcswbCattBu5LFYML5UyEmQi1J-870R8TwLMiXHOTedwIdJg0grDN815m3jbOVF0Mb0JzcaE7CDUOf9ttuV8RFWq9lOVdJcxYOayICf3v2wWgyKEe6oj3PN8_m4AyJT7X"
        mockSignup(empId = empId, fcmToken = dummyFCM)
    }

    /**
     * Mock signup for bypass mode.
     */
    private fun mockSignup(empId: String, fcmToken: String) {
        viewModelScope.launch {
            _signInUiState.update { it.copy(loading = true) }

            val result = authRepository.login(LoginPostModel(userName = empId, fcmToken = fcmToken))

            result.fold(
                onSuccess = { response ->
                    loginModel = loginModel.copy(
                        empId = empId,
                        accessToken = response.token,
                        refreshToken = response.refreshToken,
                        otp = response.otpCode?.toString() ?: "",
                        isLoggedIn = true,
                        isSignedUp = true,
                    )

                    authRepository.saveLoginModel(loginModel)
                    // TODO: Clear shared preferences if needed

                    _signInUiState.update {
                        it.copy(
                            loading = false,
                            data = it.data.copy(isSignUpSuccessful = true),
                        )
                    }
                },
                onFailure = { error ->
                    _signInUiState.update {
                        it.copy(
                            loading = false,
                            error = OneTimeEvent(error),
                        )
                    }
                },
            )
        }
    }

    /**
     * Device login with saved credentials.
     */
    fun deviceLogin(user: String, password: String, openDashboard: () -> Unit) {
        when (loginModel.isSignedUp) {
            true -> {
                when (password.isNotEmpty() && loginModel.password == password && loginModel.empId == user) {
                    true -> {
                        loginModel = loginModel.copy(isLoggedIn = true)
                        viewModelScope.launch {
                            authRepository.saveLoginModel(loginModel)
                            openDashboard()
                        }
                    }
                    false -> {
                        _signInUiState.update {
                            it.copy(
                                error = OneTimeEvent(Exception("Credentials don't match")),
                            )
                        }
                    }
                }
            }
            false -> {
                _signInUiState.update {
                    it.copy(
                        error = OneTimeEvent(Exception("You are not logged in, please sign up")),
                    )
                }
            }
        }
    }

    /**
     * Shows sign up UI (hides login button, shows verify button).
     */
    fun showSignUpUi() {
        _signInUiState.updateState {
            copy(
                isLoginBtnVisible = false,
                isSignUpBtnVisible = false,
                isVerifyBtnVisible = true,
            )
        }
    }

    /**
     * Updates the bypass OTP setting.
     *
     * @param enabled Whether to enable bypass OTP mode.
     */
    fun updateBypassOTP(enabled: Boolean) {
        _signInUiState.updateState {
            copy(isBypassOTP = enabled)
        }
    }

    /**
     * Deletes all data (for testing/debugging).
     */
    fun deleteAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            // TODO: Implement data deletion if needed
            authRepository.clearLoginModel()
        }
    }
}
