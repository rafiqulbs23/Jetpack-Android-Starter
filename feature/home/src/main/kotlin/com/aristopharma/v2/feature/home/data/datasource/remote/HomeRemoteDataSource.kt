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

package com.aristopharma.v2.feature.home.data.datasource.remote

import com.aristopharma.v2.feature.home.data.datasource.remote.api.HomeApiService
import com.aristopharma.v2.feature.home.data.datasource.remote.api.JetpackDto
import com.aristopharma.v2.firebase.firestore.model.FirebaseJetpack
import javax.inject.Inject

/**
 * Remote data source for home feature operations using API services.
 *
 * @param apiService The Retrofit API service for home endpoints.
 */
class HomeRemoteDataSource @Inject constructor(
    private val apiService: HomeApiService,
) {
    /**
     * Create or update a jetpack.
     *
     * @param jetpack The jetpack Firebase model.
     */
    suspend fun createOrUpdateJetpack(jetpack: FirebaseJetpack) {
        val dto = JetpackDto(
            id = jetpack.id,
            name = jetpack.name,
            price = jetpack.price,
            userId = jetpack.userId,
            lastUpdated = jetpack.lastUpdated,
            lastSynced = jetpack.lastSynced,
        )
        val response = apiService.createOrUpdateJetpack(dto)
        if (!response.isSuccessful) {
            throw Exception("Create/update jetpack failed: ${response.message()}")
        }
    }

    /**
     * Delete a jetpack.
     *
     * @param jetpack The jetpack Firebase model.
     */
    suspend fun deleteJetpack(jetpack: FirebaseJetpack) {
        val response = apiService.deleteJetpack(jetpack.id)
        if (!response.isSuccessful) {
            throw Exception("Delete jetpack failed: ${response.message()}")
        }
    }

    /**
     * Pull jetpacks from remote.
     *
     * @param userId The user ID.
     * @param lastSynced The last sync timestamp.
     * @return A list of jetpack Firebase models.
     */
    suspend fun pullJetpacks(userId: String, lastSynced: Long): List<FirebaseJetpack> {
        val response = apiService.getJetpacks(userId, lastSynced)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!.map { dto ->
                FirebaseJetpack(
                    id = dto.id,
                    name = dto.name,
                    price = dto.price,
                    userId = dto.userId ?: userId,
                    lastUpdated = dto.lastUpdated ?: System.currentTimeMillis(),
                    lastSynced = dto.lastSynced ?: System.currentTimeMillis(),
                    deleted = false,
                )
            }
        } else {
            throw Exception("Get jetpacks failed: ${response.message()}")
        }
    }
}

