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

package com.aristopharma.v2.core.di

import com.aristopharma.v2.core.room.di.DaoModule
import com.aristopharma.v2.core.room.di.DatabaseModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Aggregates database-related modules so app/feature scopes can include a single module.
 */
@Module(
    includes = [
        DatabaseModule::class,
        DaoModule::class,
    ],
)
@InstallIn(SingletonComponent::class)
object DatabaseModule{}

