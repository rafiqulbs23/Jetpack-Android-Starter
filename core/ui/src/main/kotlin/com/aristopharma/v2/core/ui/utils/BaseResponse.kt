package com.aristopharma.v2.core.ui.utils

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class BaseResponse<M> (
    @SerialName("status") var status: String? = null,
    @SerialName("statusCode") var statusCode: Int? = null,
    @SerialName("message") var message: String? = null,
    @SerialName("errors") var errors: ArrayList<String> = arrayListOf(),
    @SerialName("data") var data: M? = null,
)