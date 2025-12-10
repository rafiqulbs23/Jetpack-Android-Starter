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

package com.aristopharma.v2.feature.home.domain.usecase

import com.aristopharma.v2.feature.home.domain.model.Jetpack
import com.aristopharma.v2.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving a specific jetpack by ID.
 *
 * @param repository The home repository to fetch data from.
 */
class GetJetpackUseCase @Inject constructor(
    private val repository: HomeRepository,
) {
    /**
     * Executes the use case to retrieve a jetpack by its ID.
     *
     * @param id The unique identifier of the jetpack.
     * @return A Flow emitting the Jetpack domain model.
     */
    operator fun invoke(id: String): Flow<Jetpack> = repository.getJetpack(id)
}


