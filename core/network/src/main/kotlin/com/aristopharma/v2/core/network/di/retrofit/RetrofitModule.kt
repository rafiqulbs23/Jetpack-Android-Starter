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

package com.aristopharma.v2.core.network.di.retrofit

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.aristopharma.v2.core.network.BuildConfig
import com.aristopharma.v2.core.network.di.okhttp.OkHttpClientModule
import com.aristopharma.v2.core.network.utils.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Module for providing [Retrofit].
 */
@Module(
    includes = [
        OkHttpClientModule::class,
    ],
)
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    /**
     * Provides [Retrofit].
     *
     * @param converterFactory [Converter.Factory].
     * @param okHttpClient [OkHttpClient].
     * @return [Retrofit].
     */
    @Singleton
    @Provides
    fun provideRetrofitClient(
        converterFactory: Converter.Factory,
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .build()
    }
}
