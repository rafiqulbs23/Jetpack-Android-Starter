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

package com.aristopharma.v2.feature.notification.di

import com.aristopharma.v2.feature.notification.data.datasource.remote.NotificationRemoteDataSource
import com.aristopharma.v2.feature.notification.data.datasource.remote.NotificationRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing notification-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModule {
    /**
     * Binds the implementation of [NotificationRemoteDataSource] to [NotificationRemoteDataSourceImpl].
     *
     * @param notificationRemoteDataSourceImpl The implementation of [NotificationRemoteDataSource].
     * @return The bound [NotificationRemoteDataSource] instance.
     */
    @Binds
    @Singleton
    abstract fun bindNotificationRemoteDataSource(
        notificationRemoteDataSourceImpl: NotificationRemoteDataSourceImpl,
    ): NotificationRemoteDataSource
}

