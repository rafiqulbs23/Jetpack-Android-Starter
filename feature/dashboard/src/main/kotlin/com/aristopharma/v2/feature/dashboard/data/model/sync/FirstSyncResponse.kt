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

package com.aristopharma.v2.feature.dashboard.data.model.sync

import com.aristopharma.v2.feature.dashboard.data.model.MenuPermission
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * First sync response model.
 * Contains all data synced from server on first login.
 */
@Serializable
data class FirstSyncResponse(
    @SerialName("employeeInfo") val employeeInfo: EmployeeInfoModel? = null,
    @SerialName("productInfos") val productInfos: List<ProductInfoModel> = emptyList(),
    @SerialName("chInfos") val chemistInfos: List<ChemistInfoModel> = emptyList(),
    @SerialName("orderTypes") val orderTypes: List<OrderTypeModel> = emptyList(),
    @SerialName("payOptions") val paymentOptions: List<PaymentOptionModel> = emptyList(),
    @SerialName("mobileMenuPermissions") val menuPermissions: List<MenuPermission> = emptyList()
)
