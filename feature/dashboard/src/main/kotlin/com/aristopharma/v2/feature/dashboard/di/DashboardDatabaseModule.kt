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

import androidx.room.Room
import android.content.Context
import com.aristopharma.v2.feature.dashboard.data.local.dao.DashboardDao
import com.aristopharma.v2.feature.dashboard.data.local.entity.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Local database for Dashboard feature.
 */
@Database(
    entities = [
        EmployeeInfoEntity::class,
        ProductInfoEntity::class,
        ChemistInfoEntity::class,
        MenuPermissionEntity::class,
        DashboardSummaryEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class DashboardDatabase : RoomDatabase() {
    abstract fun dashboardDao(): DashboardDao
}

/**
 * Hilt module for providing dashboard database dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object DashboardDatabaseModule {
    
    @Provides
    @Singleton
    fun provideDashboardDatabase(
        @ApplicationContext context: Context
    ): DashboardDatabase {
        return Room.databaseBuilder(
            context,
            DashboardDatabase::class.java,
            "dashboard_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    
    @Provides
    @Singleton
    fun provideDashboardDao(database: DashboardDatabase): DashboardDao {
        return database.dashboardDao()
    }
}
