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

package com.aristopharma.v2.core.network.di.okhttp

import com.aristopharma.v2.core.network.auth.TokenAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Singleton

/**
 * Module for providing [OkHttpClient].
 */
@Module(
    includes = [
        InterceptorModule::class,
    ],
)
@InstallIn(SingletonComponent::class)
object OkHttpClientModule {

    private const val TIME_OUT = 60L

    /**
     * Provides [OkHttpClient] with interceptors and token authenticator.
     *
     * @param loggingInterceptor [HttpLoggingInterceptor] for logging requests/responses.
     * @param tokenAuthenticator [TokenAuthenticator] for automatic token refresh.
     * @return [OkHttpClient] configured with timeouts, interceptors, and authenticator.
     */
    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        tokenAuthenticator: TokenAuthenticator,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(TIME_OUT, SECONDS)
            .readTimeout(TIME_OUT, SECONDS)
            .writeTimeout(TIME_OUT, SECONDS)
            .addInterceptor(loggingInterceptor)
            .authenticator(tokenAuthenticator)
            .build()
    }

    /**
     * Provides [Call.Factory] for Coil image loading.
     *
     * @param loggingInterceptor [HttpLoggingInterceptor] for logging.
     * @return [Call.Factory] configured with timeouts and logging.
     */
    @Provides
    @Singleton
    fun provideOkHttpCallFactory(
        loggingInterceptor: HttpLoggingInterceptor,
    ): Call.Factory {
        return OkHttpClient.Builder()
            .connectTimeout(TIME_OUT, SECONDS)
            .readTimeout(TIME_OUT, SECONDS)
            .writeTimeout(TIME_OUT, SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }
}
