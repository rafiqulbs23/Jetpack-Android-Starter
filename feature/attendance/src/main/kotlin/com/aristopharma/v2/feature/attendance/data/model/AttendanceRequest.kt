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

package com.aristopharma.v2.feature.attendance.data.model

import kotlinx.serialization.Serializable

/**
 * API request model for check-in.
 */
@Serializable
data class CheckInRequest(
    val empId: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: String
)

/**
 * API request model for check-out.
 */
@Serializable
data class CheckOutRequest(
    val empId: String,
    val attendanceId: String,
    val timestamp: String
)
