package com.aristopharma.v2.feature.auth.data.datasource.remote

import com.aristopharma.v2.core.ui.utils.BaseResponse
import com.aristopharma.v2.feature.auth.data.model.LoginPostModel
import com.aristopharma.v2.feature.auth.data.model.LoginResponseModel
import com.aristopharma.v2.feature.auth.data.model.OTPValidationRequest
import com.aristopharma.v2.feature.auth.data.model.OTPValidationResponse
import javax.inject.Inject

/**
 * Implementation of [AuthRemoteDataSource] that uses Retrofit API service.
 */
class AuthRemoteDataSourceImpl @Inject constructor(
    private val apiService: AuthApiService,
) : AuthRemoteDataSource {
    override suspend fun login(model: LoginPostModel): BaseResponse<LoginResponseModel>? {
        return try {
            apiService.login(model)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun validateOTP(model: OTPValidationRequest): BaseResponse<OTPValidationResponse>? {
        return try {
            apiService.validateOTP(model)
        } catch (e: Exception) {
            null
        }
    }
}

