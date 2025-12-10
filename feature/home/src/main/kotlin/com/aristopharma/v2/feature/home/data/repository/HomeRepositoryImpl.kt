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

package com.aristopharma.v2.feature.home.data.repository

import com.aristopharma.v2.core.room.model.SyncAction
import com.aristopharma.v2.core.sync.SyncProgress
import com.aristopharma.v2.core.utils.suspendRunCatching
import com.aristopharma.v2.feature.home.data.datasource.local.HomeLocalDataSource
import com.aristopharma.v2.feature.home.data.datasource.remote.HomeRemoteDataSource
import com.aristopharma.v2.feature.home.data.mapper.toDomain
import com.aristopharma.v2.feature.home.data.mapper.toEntity
import com.aristopharma.v2.feature.home.data.mapper.toFirebase
import com.aristopharma.v2.feature.home.domain.model.Jetpack
import com.aristopharma.v2.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

/**
 * Implementation of [HomeRepository] for the data layer.
 *
 * @param localDataSource The local data source for Room database operations.
 * @param remoteDataSource The remote data source for Firebase operations.
 */
class HomeRepositoryImpl @Inject constructor(
    private val localDataSource: HomeLocalDataSource,
    private val remoteDataSource: HomeRemoteDataSource,
) : HomeRepository {

    /**
     * Get all jetpacks.
     *
     * @return A [Flow] of a list of domain [Jetpack] models.
     */
    override fun getJetpacks(): Flow<List<Jetpack>> {
        return localDataSource.getJetpacks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    /**
     * Get a jetpack by its ID.
     *
     * @param id The ID of the jetpack.
     * @return A [Flow] of a domain [Jetpack] model.
     */
    override fun getJetpack(id: String): Flow<Jetpack> {
        return localDataSource.getJetpack(id).map { it.toDomain() }
    }

    /**
     * Create or update a jetpack.
     *
     * @param jetpack The domain jetpack model to create or update.
     * @return A [Result] indicating the success or failure of the operation.
     */
    override suspend fun createOrUpdateJetpack(jetpack: Jetpack): Result<Unit> {
        return suspendRunCatching {
           // val userId = localDataSource.getUserId()
            localDataSource.upsertJetpack(
                jetpack
                    .toEntity()
                    .copy(
                        userId = "",
                        lastUpdated = System.currentTimeMillis(),
                        needsSync = true,
                        syncAction = SyncAction.UPSERT,
                    ),
            )
        }
    }

    /**
     * Mark a jetpack as deleted.
     *
     * @param jetpack The domain jetpack model to mark as deleted.
     * @return A [Result] indicating the success or failure of the operation.
     */
    override suspend fun markJetpackAsDeleted(jetpack: Jetpack): Result<Unit> {
        return suspendRunCatching {
            localDataSource.markJetpackAsDeleted(jetpack.id)
        }
    }

    /**
     * Sync jetpacks with the remote data source.
     *
     * @return A [Flow] of [SyncProgress].
     */
    override suspend fun sync(): Flow<SyncProgress> {
        return flow {
            // Unsynced jetpacks
            val unsyncedJetpacks = localDataSource.getUnsyncedJetpacks()
            val totalUnsyncedJetpacks = unsyncedJetpacks.size
            Timber.d("Syncing $totalUnsyncedJetpacks unsynced jetpacks")

            // Push updates to remote
            unsyncedJetpacks.forEachIndexed { index, unsyncedJetpack ->
                when (unsyncedJetpack.syncAction) {
                    SyncAction.UPSERT -> {
                        Timber.d("Syncing create/update jetpack: ${unsyncedJetpack.id}")
                        remoteDataSource.createOrUpdateJetpack(
                            unsyncedJetpack
                                .toFirebase()
                                .copy(
                                    lastSynced = System.currentTimeMillis(),
                                ),
                        )
                    }

                    SyncAction.DELETE -> {
                        remoteDataSource.deleteJetpack(
                            unsyncedJetpack
                                .toFirebase()
                                .copy(
                                    lastSynced = System.currentTimeMillis(),
                                ),
                        )
                    }

                    SyncAction.NONE -> {
                        // Do nothing
                    }
                }

                localDataSource.markAsSynced(unsyncedJetpack.id)
                emit(
                    SyncProgress(
                        total = totalUnsyncedJetpacks,
                        current = index + 1,
                        message = "Syncing jetpacks with the cloud",
                    ),
                )
            }

            // Remote jetpacks
            val userId = ""//localDataSource.getUserId()
            val lastSynced = localDataSource.getLatestUpdateTimestamp()
            val remoteJetpacks = remoteDataSource.pullJetpacks(userId, lastSynced)
            val totalRemoteJetpacks = remoteJetpacks.size
            Timber.d("Syncing $totalRemoteJetpacks remote jetpacks")

            // Pull updates from remote
            // We pull after pushing local changes to save the local changes to the cloud first
            // and avoid accidentally overwriting them with stale data
            remoteJetpacks.forEachIndexed { index, remoteJetpack ->
                localDataSource.upsertJetpack(remoteJetpack.toEntity())
                emit(
                    SyncProgress(
                        total = totalRemoteJetpacks,
                        current = index + 1,
                        message = "Fetching jetpacks from the cloud",
                    ),
                )
            }
        }
    }
}

