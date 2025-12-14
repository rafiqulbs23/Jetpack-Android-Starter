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

package com.aristopharma.v2.feature.auth.data.repository

import com.aristopharma.v2.core.ui.utils.BaseResponse
import com.aristopharma.v2.core.utils.suspendRunCatching
import com.aristopharma.v2.feature.auth.data.datasource.local.AuthLocalDataSource
import com.aristopharma.v2.feature.auth.data.datasource.remote.AuthRemoteDataSource
import com.aristopharma.v2.feature.auth.data.model.LoginModel
import com.aristopharma.v2.feature.auth.data.model.LoginPostModel
import com.aristopharma.v2.feature.auth.data.model.LoginResponseModel
import com.aristopharma.v2.feature.auth.data.model.OTPValidationRequest
import com.aristopharma.v2.feature.auth.data.model.OTPValidationResponse
import com.aristopharma.v2.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Implementation of the [AuthRepository] interface responsible for handling authentication operations.
 *
 * @param remoteDataSource The remote data source for authentication operations.
 * @param localDataSource The local data source for storing user data.
 */
class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource,
    private val localDataSource: AuthLocalDataSource,
) : AuthRepository {

    override suspend fun login(model: LoginPostModel): Result<LoginResponseModel> {
        return suspendRunCatching {
            val response = remoteDataSource.login(model)
                ?: throw IllegalStateException("No response received from server")
            
            validateAndExtractResponse(
                response = response,
                operationName = "Login",
            )
        }
    }

    override suspend fun validateOTP(model: OTPValidationRequest): Result<OTPValidationResponse> {
        return suspendRunCatching {
            val response = remoteDataSource.validateOTP(model)
                ?: throw IllegalStateException("No response received from server")
            
            validateAndExtractResponse(
                response = response,
                operationName = "OTP validation",
            )
        }
    }

    /**
     * Validates the response and extracts the data if successful.
     *
     * @param response The response to validate.
     * @param operationName The name of the operation for error messages.
     * @return The data from the response if successful.
     * @throws IllegalStateException if the response indicates failure.
     */
    private fun <T> validateAndExtractResponse(
        response: BaseResponse<T>,
        operationName: String,
    ): T {
        // Check for success case first
        if (response.statusCode in 200..299 && response.data != null) {
            return response.data as T
        }
        
        // Check for HTTP error status codes
        if (response.statusCode != null && response.statusCode !in 200..299) {
            val errorMessage = getErrorMessage(
                response,
                operationName,
                "Status code ${response.statusCode}",
            )
            throw IllegalStateException(errorMessage)
        }
        
        // Check for missing data
        if (response.data == null) {
            val errorMessage = getErrorMessage(response, operationName, "No data received")
            throw IllegalStateException(errorMessage)
        }
        
        // Unknown error case
        throw IllegalStateException("$operationName failed: Unknown error")
    }

    /**
     * Extracts error message from response with fallback options.
     *
     * @param response The response containing error information.
     * @param operationName The name of the operation.
     * @param fallbackMessage The fallback message if no error details are available.
     * @return The error message.
     */
    private fun <T> getErrorMessage(
        response: BaseResponse<T>,
        operationName: String,
        fallbackMessage: String,
    ): String {
        return response.message
            ?: response.errors.takeIf { it.isNotEmpty() }?.joinToString(", ")
            ?: "$operationName failed: $fallbackMessage"
    }

    override suspend fun saveLoginModel(loginModel: LoginModel) {
        try {
            localDataSource.saveLoginModel(loginModel)
        } catch (e: Exception) {
            throw IllegalStateException("Failed to save login model", e)
        }
    }

    override suspend fun getLoginModel(): LoginModel? {
        return try {
            localDataSource.getLoginModel()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun clearLoginModel() {
        try {
            localDataSource.clearLoginModel()
        } catch (e: Exception) {
            throw IllegalStateException("Failed to clear login model", e)
        }
    }

    override suspend fun isLoggedIn(): Boolean {
        return try {
            localDataSource.isLoggedIn()
        } catch (e: Exception) {
            false
        }
    }
}

