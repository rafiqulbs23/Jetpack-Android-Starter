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

package com.aristopharma.v2.compose.di

import com.aristopharma.v2.compose.BuildConfig
import com.aristopharma.v2.core.di.BuildConfigModule
import com.aristopharma.v2.core.di.EnableBypassOtp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * App module override for BuildConfig values.
 * 
 * This module overrides the default BuildConfigModule to provide actual BuildConfig values
 * from the app module's BuildConfig class.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppBuildConfigModule {
    /**
     * Provides the ENABLE_BYPASS_OTP flag from the app's BuildConfig.
     * 
     * This overrides the default value provided in [BuildConfigModule].
     * 
     * @return The ENABLE_BYPASS_OTP flag value from BuildConfig.
     */
    @Provides
    @Singleton
    @EnableBypassOtp
    fun provideEnableBypassOtp(): Boolean {
        return BuildConfig.ENABLE_BYPASS_OTP
    }
}

