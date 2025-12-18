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

import com.aristopharma.v2.core.preferences.data.UserPreferencesDataSource
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Entry point for accessing navigation-related dependencies from Hilt.
 * 
 * This allows feature modules to access core dependencies without creating
 * circular dependencies. Use with EntryPointAccessors.fromApplication().
 * 
 * Example usage:
 * ```kotlin
 * val entryPoint = EntryPointAccessors.fromApplication(
 *     context.applicationContext,
 *     NavigationEntryPoint::class.java
 * )
 * val userPreferences = entryPoint.userPreferencesDataSource()
 * ```
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface NavigationEntryPoint {
    /**
     * Provides access to UserPreferencesDataSource for checking authentication status.
     */
    fun userPreferencesDataSource(): UserPreferencesDataSource
}
