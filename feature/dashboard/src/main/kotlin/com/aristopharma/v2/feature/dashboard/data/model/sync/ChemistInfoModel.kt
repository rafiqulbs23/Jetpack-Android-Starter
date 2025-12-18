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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Chemist/Customer information model from first sync.
 * Contains customer details for order management.
 */
@Serializable
data class ChemistInfoModel(
    @SerialName("chId") val chemistId: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("chType") val chemistType: String? = null,
    @SerialName("addr") val address: String? = null,
    @SerialName("spDiscount") val specialDiscount: String? = null,
    @SerialName("dId") val distributorId: Int? = null,
    @SerialName("id") val id: Int? = null
)
