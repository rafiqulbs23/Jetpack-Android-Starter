package com.aristopharma.v2.feature.auth.data.datasource.remote

import com.aristopharma.v2.core.ui.utils.BaseResponse
import com.aristopharma.v2.feature.auth.data.model.LoginPostModel
import com.aristopharma.v2.feature.auth.data.model.LoginResponseModel
import com.aristopharma.v2.feature.auth.data.model.OTPValidationRequest
import com.aristopharma.v2.feature.auth.data.model.OTPValidationResponse

/**
 * Remote data source interface for authentication operations.
 */
interface AuthRemoteDataSource {
    suspend fun login(model: LoginPostModel): BaseResponse<LoginResponseModel>?
    suspend fun validateOTP(model: OTPValidationRequest): BaseResponse<OTPValidationResponse>?
}

