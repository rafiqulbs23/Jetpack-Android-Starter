package com.aristopharma.v2.feature.auth.data.model

import com.aristopharma.v2.core.ui.utils.AppConst.EMPTY_VALUE
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseModel(
    @SerialName("empId") val empId: Int? = null,
    @SerialName("name") val name: String = EMPTY_VALUE,
    @SerialName("mobileNo") val mobileNo: String = EMPTY_VALUE,
    @SerialName("token") val token: String = EMPTY_VALUE,
    @SerialName("refreshToken") val refreshToken: String = EMPTY_VALUE,
    @SerialName("otpCode") val otpCode: Int? = null,
    @SerialName("isFirstLogin") val isFirstLogin: Boolean = false,
    @SerialName("userRoleType") val userRoleType: String = EMPTY_VALUE,
)