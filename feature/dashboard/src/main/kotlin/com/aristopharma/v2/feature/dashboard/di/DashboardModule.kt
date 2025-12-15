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

package com.aristopharma.v2.feature.dashboard.di

import com.aristopharma.v2.feature.dashboard.data.datasource.local.DashboardLocalDataSource
import com.aristopharma.v2.feature.dashboard.data.datasource.local.DashboardLocalDataSourceImpl
import com.aristopharma.v2.feature.dashboard.data.datasource.remote.DashboardRemoteDataSource
import com.aristopharma.v2.feature.dashboard.data.datasource.remote.DashboardRemoteDataSourceImpl
import com.aristopharma.v2.feature.dashboard.data.repository.DashboardRepositoryImpl
import com.aristopharma.v2.feature.dashboard.domain.repository.DashboardRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing dashboard-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DashboardModule {
    /**
     * Binds the implementation of [DashboardRemoteDataSource] to [DashboardRemoteDataSourceImpl].
     */
    @Binds
    @Singleton
    abstract fun bindDashboardRemoteDataSource(
        dashboardRemoteDataSourceImpl: DashboardRemoteDataSourceImpl,
    ): DashboardRemoteDataSource

    /**
     * Binds the implementation of [DashboardLocalDataSource] to [DashboardLocalDataSourceImpl].
     */
    @Binds
    @Singleton
    abstract fun bindDashboardLocalDataSource(
        dashboardLocalDataSourceImpl: DashboardLocalDataSourceImpl,
    ): DashboardLocalDataSource

    /**
     * Binds the implementation of [DashboardRepository] to [DashboardRepositoryImpl].
     */
    @Binds
    @Singleton
    abstract fun bindDashboardRepository(
        dashboardRepositoryImpl: DashboardRepositoryImpl,
    ): DashboardRepository
}

