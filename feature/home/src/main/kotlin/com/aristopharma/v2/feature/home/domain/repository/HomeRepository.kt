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

package com.aristopharma.v2.feature.home.domain.repository

import com.aristopharma.v2.feature.home.domain.model.Jetpack
import com.aristopharma.v2.core.sync.SyncProgress
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for home feature domain operations.
 * This defines the contract for data operations in the domain layer.
 */
interface HomeRepository {
    /**
     * Retrieves a list of all jetpacks.
     *
     * @return A Flow emitting a list of Jetpack domain models.
     */
    fun getJetpacks(): Flow<List<Jetpack>>

    /**
     * Retrieves a specific jetpack by its ID.
     *
     * @param id The unique identifier of the jetpack.
     * @return A Flow emitting the Jetpack domain model.
     */
    fun getJetpack(id: String): Flow<Jetpack>

    /**
     * Creates or updates a jetpack in the repository.
     *
     * @param jetpack The Jetpack domain model to create or update.
     * @return A Result indicating the success or failure of the operation.
     */
    suspend fun createOrUpdateJetpack(jetpack: Jetpack): Result<Unit>

    /**
     * Marks a jetpack as deleted in the repository.
     *
     * @param jetpack The Jetpack domain model to mark as deleted.
     * @return A Result indicating the success or failure of the operation.
     */
    suspend fun markJetpackAsDeleted(jetpack: Jetpack): Result<Unit>

    /**
     * Synchronizes data and returns a Flow emitting the progress of the sync operation.
     *
     * @return A Flow emitting SyncProgress objects representing the progress of the sync operation.
     */
    suspend fun sync(): Flow<SyncProgress>
}

