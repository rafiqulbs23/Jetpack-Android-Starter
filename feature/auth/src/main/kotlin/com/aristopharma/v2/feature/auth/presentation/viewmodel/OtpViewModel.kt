package com.aristopharma.v2.feature.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aristopharma.v2.feature.auth.BuildConfig
import com.aristopharma.v2.feature.auth.data.model.LoginModel
import com.aristopharma.v2.feature.auth.data.model.OTPValidationRequest
import com.aristopharma.v2.feature.auth.domain.model.OtpEvent
import com.aristopharma.v2.feature.auth.domain.model.OtpUiState
import com.aristopharma.v2.feature.auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for OTP verification screen.
 * 
 * Handles OTP input, validation, and auto-fill from SMS.
 * Saves login credentials after successful OTP verification.
 */
@HiltViewModel
class OtpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferencesDataSource: com.aristopharma.v2.core.preferences.data.UserPreferencesDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow(OtpUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.value = _uiState.value.copy(
            isAutoFilledEnable = BuildConfig.IS_AUTOMIC_OTP_ENABLE ?: true
        )
    }

    /**
     * Set OTP value in the UI state.
     * 
     * @param otp The OTP value to set.
     */
    fun setOtp(otp: String) {
        _uiState.value = _uiState.value.copy(otp = otp)
    }

    /**
     * Verify OTP with the server.
     * 
     * @param username The employee ID.
     * @param otp The OTP code to verify.
     * @param password The password (for saving after successful verification).
     */
    fun verifyOtp(username: String, otp: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            // Convert username and otp to integers
            val empIdInt = username.toIntOrNull()
            val otpInt = otp.toIntOrNull()
            
            if (empIdInt == null || otpInt == null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Invalid employee ID or OTP format"
                    )
                }
                return@launch
            }
            
            // Call the API to validate OTP
            val result = authRepository.validateOTP(
                OTPValidationRequest(emp_Id = empIdInt, otp = otpInt)
            )
            
            result.fold(
                onSuccess = { response ->
                    if (response.is_Validated) {
                        // OTP validation successful - save login credentials
                        saveLoginCredentials(username, password)
                    } else {
                        // OTP validation failed
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = "OTP validation failed"
                            )
                        }
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Unknown error occurred"
                        )
                    }
                }
            )
        }
    }

    /**
     * Save login credentials after successful OTP verification.
     * 
     * @param username The employee ID.
     * @param password The password.
     */
    private fun saveLoginCredentials(username: String, password: String) {
        viewModelScope.launch {
            try {
                // Get existing login model or create new one
                val existingModel = authRepository.getLoginModel()
                
                // Update login model with verified credentials
                val loginModel = (existingModel ?: LoginModel()).copy(
                    empId = username,
                    password = password,
                    isSignedUp = true,
                    isLoggedIn = true
                )
                
                // Save to local storage (auth repository)
                authRepository.saveLoginModel(loginModel)
                
                // Save user profile to preferences with tokens
                userPreferencesDataSource.setUserProfile(
                    com.aristopharma.v2.core.preferences.model.PreferencesUserProfile(
                        id = username,
                        userName = loginModel.empName,
                        accessToken = loginModel.accessToken,
                        refreshToken = loginModel.refreshToken,
                        fcmToken = loginModel.fcmToken,
                        profilePictureUriString = null
                    )
                )
                
                // Update UI state to navigate to home
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    navigateToHome = true
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to save login credentials: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * Extract OTP from SMS message.
     * 
     * @param message The SMS message containing the OTP.
     * @return The extracted OTP, or empty string if not found.
     */
    fun extractOtpFromMessage(message: String): String {
        val pattern = Regex("\\b\\d{6}\\b") // Matches a 6-digit number
        return pattern.find(message)?.value ?: ""
    }

    /**
     * Handle OTP screen events.
     * 
     * @param event The event to handle.
     */
    fun onEvent(event: OtpEvent) {
        when (event) {
            is OtpEvent.OnChangeOtpButtonStatus -> {
                _uiState.update {
                    it.copy(
                        isOtpButtonEnable = event.isEnabled
                    )
                }
            }
        }
    }
}
