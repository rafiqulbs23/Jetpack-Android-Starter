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

package com.aristopharma.v2.feature.home.data.datasource.local

import com.aristopharma.v2.core.preferences.data.UserPreferencesDataSource
import com.aristopharma.v2.core.room.data.LocalDataSource
import com.aristopharma.v2.core.room.model.JetpackEntity
import com.aristopharma.v2.core.room.model.SyncAction
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Local data source for home feature operations.
 *
 * @param localDataSource The Room database local data source.
 * @param preferencesDataSource The user preferences data source.
 */
class HomeLocalDataSource @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val preferencesDataSource: UserPreferencesDataSource,
) {
    /**
     * Get all jetpacks for the current user.
     *
     * @return A Flow of jetpack entities.
     */
    fun getJetpacks(): Flow<List<JetpackEntity>> {
       // val userId = preferencesDataSource.getUserIdOrThrow()
        return localDataSource.getJetpacks("56")
    }

    /**
     * Get a jetpack by its ID.
     *
     * @param id The jetpack ID.
     * @return A Flow of the jetpack entity.
     */
    fun getJetpack(id: String): Flow<JetpackEntity> {
        return localDataSource.getJetpack(id)
    }

    /**
     * Upsert a jetpack entity.
     *
     * @param jetpack The jetpack entity to upsert.
     */
    suspend fun upsertJetpack(jetpack: JetpackEntity) {
        localDataSource.upsertJetpack(jetpack)
    }

    /**
     * Mark a jetpack as deleted.
     *
     * @param jetpackId The jetpack ID.
     */
    suspend fun markJetpackAsDeleted(jetpackId: String) {
        localDataSource.markJetpackAsDeleted(jetpackId)
    }

    /**
     * Get unsynced jetpacks for the current user.
     *
     * @return A list of unsynced jetpack entities.
     */
    suspend fun getUnsyncedJetpacks(): List<JetpackEntity> {
        val userId = preferencesDataSource.getUserIdOrThrow()
        return localDataSource.getUnsyncedJetpacks(userId)
    }

    /**
     * Mark a jetpack as synced.
     *
     * @param jetpackId The jetpack ID.
     */
    suspend fun markAsSynced(jetpackId: String) {
        localDataSource.markAsSynced(jetpackId)
    }

    /**
     * Get the latest update timestamp for the current user.
     *
     * @return The latest update timestamp.
     */
    suspend fun getLatestUpdateTimestamp(): Long {
        val userId = preferencesDataSource.getUserIdOrThrow()
        return localDataSource.getLatestUpdateTimestamp(userId)
    }

    /**
     * Get the current user ID.
     *
     * @return The user ID.
     */
   /* fun getUserId(): String {
        return preferencesDataSource.getUserIdOrThrow()
    }*/
}

