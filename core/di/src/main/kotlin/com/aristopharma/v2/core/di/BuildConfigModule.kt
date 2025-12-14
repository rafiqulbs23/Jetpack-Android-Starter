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

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Qualifier for ENABLE_BYPASS_OTP BuildConfig value.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class EnableBypassOtp

/**
 * Dagger module for providing BuildConfig values.
 * 
 * Note: This module should be overridden in the app module to provide actual BuildConfig values.
 * For now, it provides default values. The app module should provide the actual values.
 */
@Module
@InstallIn(SingletonComponent::class)
object BuildConfigModule {
    /**
     * Provides the ENABLE_BYPASS_OTP flag from BuildConfig.
     * 
     * Default value is false. Override this in the app module to provide the actual BuildConfig value.
     * 
     * @return The ENABLE_BYPASS_OTP flag value.
     */
    @Provides
    @Singleton
    @EnableBypassOtp
    fun provideEnableBypassOtp(): Boolean {
        // Default value - should be overridden in app module
        return false
    }
}

