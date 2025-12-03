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

package com.aristopharma.v2.feature.home.domain.model

import java.util.UUID

/**
 * Domain model representing a Jetpack.
 * This is the core business entity used throughout the home feature.
 *
 * @param id The unique identifier of the Jetpack.
 * @param name The name of the Jetpack.
 * @param price The price of the Jetpack.
 * @param lastUpdated The last updated timestamp of the Jetpack.
 * @param lastSynced The last synced timestamp of the Jetpack.
 * @param needsSync The sync status of the Jetpack.
 * @param formattedDate The formatted date of the Jetpack.
 */
data class Jetpack(
    val id: String = UUID.randomUUID().toString(),
    val name: String = String(),
    val price: Double = 0.0,
    val lastUpdated: Long = System.currentTimeMillis(),
    val lastSynced: Long = 0L,
    val needsSync: Boolean = true,
    val formattedDate: String = String(),
)

