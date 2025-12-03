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

package com.aristopharma.v2.feature.home.data.mapper

import com.aristopharma.v2.core.extensions.asFormattedDateTime
import com.aristopharma.v2.core.room.model.JetpackEntity
import com.aristopharma.v2.feature.home.domain.model.Jetpack
import com.aristopharma.v2.firebase.firestore.model.FirebaseJetpack

/**
 * Mapper functions for converting between domain models and data models.
 */

/**
 * Maps a [JetpackEntity] to a domain [Jetpack].
 */
fun JetpackEntity.toDomain(): Jetpack {
    return Jetpack(
        id = id,
        name = name,
        price = price,
        lastUpdated = lastUpdated,
        lastSynced = lastSynced,
        needsSync = needsSync,
        formattedDate = lastUpdated.asFormattedDateTime(),
    )
}

/**
 * Maps a list of [JetpackEntity] to a list of domain [Jetpack].
 */
fun List<JetpackEntity>.toDomain(): List<Jetpack> {
    return map(JetpackEntity::toDomain)
}

/**
 * Maps a domain [Jetpack] to a [JetpackEntity].
 */
fun Jetpack.toEntity(): JetpackEntity {
    return JetpackEntity(
        id = id,
        name = name,
        price = price,
        lastUpdated = lastUpdated,
        lastSynced = lastSynced,
    )
}

/**
 * Maps a domain [Jetpack] to a [FirebaseJetpack].
 */
fun Jetpack.toFirebase(): FirebaseJetpack {
    return FirebaseJetpack(
        id = id,
        name = name,
        price = price,
        lastUpdated = lastUpdated,
        lastSynced = lastSynced,
    )
}

/**
 * Maps a [JetpackEntity] to a [FirebaseJetpack].
 */
fun JetpackEntity.toFirebase(): FirebaseJetpack {
    return FirebaseJetpack(
        id = id,
        name = name,
        price = price,
        userId = userId,
        lastUpdated = lastUpdated,
        lastSynced = lastSynced,
        deleted = deleted,
    )
}

/**
 * Maps a [FirebaseJetpack] to a [JetpackEntity].
 */
fun FirebaseJetpack.toEntity(): JetpackEntity {
    return JetpackEntity(
        id = id,
        name = name,
        price = price,
        userId = userId,
        lastUpdated = lastUpdated,
        lastSynced = lastSynced,
        deleted = deleted,
    )
}

