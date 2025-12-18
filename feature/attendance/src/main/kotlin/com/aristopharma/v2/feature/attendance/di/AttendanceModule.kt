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

package com.aristopharma.v2.feature.attendance.di

import com.aristopharma.v2.core.database.AppDatabase
import com.aristopharma.v2.feature.attendance.data.datasource.local.AttendanceLocalDataSource
import com.aristopharma.v2.feature.attendance.data.datasource.remote.AttendanceApiService
import com.aristopharma.v2.feature.attendance.data.datasource.remote.AttendanceRemoteDataSource
import com.aristopharma.v2.feature.attendance.data.local.dao.AttendanceDao
import com.aristopharma.v2.feature.attendance.data.repository.AttendanceRepositoryImpl
import com.aristopharma.v2.feature.attendance.domain.repository.AttendanceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Hilt module for attendance dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object AttendanceModule {
    
    @Provides
    @Singleton
    fun provideAttendanceApiService(retrofit: Retrofit): AttendanceApiService {
        return retrofit.create(AttendanceApiService::class.java)
    }
    
    @Provides
    fun provideAttendanceDao(database: AppDatabase): AttendanceDao {
        return database.attendanceDao()
    }
    
    @Provides
    @Singleton
    fun provideAttendanceRepository(
        remoteDataSource: AttendanceRemoteDataSource,
        localDataSource: AttendanceLocalDataSource
    ): AttendanceRepository {
        return AttendanceRepositoryImpl(remoteDataSource, localDataSource)
    }
}
