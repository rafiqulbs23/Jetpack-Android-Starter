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

package com.aristopharma.v2.feature.dashboard.data.repository

import com.aristopharma.v2.core.preferences.data.UserPreferencesDataSource
import com.aristopharma.v2.feature.dashboard.data.datasource.local.DashboardLocalDataSource
import com.aristopharma.v2.feature.dashboard.data.datasource.remote.DashboardRemoteDataSource
import com.aristopharma.v2.feature.dashboard.data.model.DashboardSummary
import com.aristopharma.v2.feature.dashboard.data.model.sync.FirstSyncResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Unit tests for DashboardRepositoryImpl.
 */
class DashboardRepositoryImplTest {

    private lateinit var repository: DashboardRepositoryImpl
    private lateinit var remoteDataSource: DashboardRemoteDataSource
    private lateinit var localDataSource: DashboardLocalDataSource
    private lateinit var userPreferencesDataSource: UserPreferencesDataSource

    @Before
    fun setup() {
        remoteDataSource = mockk(relaxed = true)
        localDataSource = mockk(relaxed = true)
        userPreferencesDataSource = mockk(relaxed = true)
        
        repository = DashboardRepositoryImpl(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            userPreferencesDataSource = userPreferencesDataSource
        )
    }

    @Test
    fun `performFirstSync success should save data and update preferences`() = runTest {
        // Given
        val empId = "EMP001"
        val syncResponse = mockk<FirstSyncResponse>(relaxed = true)
        coEvery { remoteDataSource.getFirstSync(empId) } returns syncResponse
        coEvery { userPreferencesDataSource.setFirstSyncDone(true) } returns Unit
        
        // When
        val result = repository.performFirstSync(empId)
        
        // Then
        assertTrue(result.isSuccess)
        coVerify { remoteDataSource.getFirstSync(empId) }
        coVerify { localDataSource.saveFirstSyncData(syncResponse) }
        coVerify { userPreferencesDataSource.setFirstSyncDone(true) }
    }

    @Test
    fun `performFirstSync failure should return error`() = runTest {
        // Given
        val empId = "EMP001"
        val error = Exception("Network error")
        coEvery { remoteDataSource.getFirstSync(empId) } throws error
        
        // When
        val result = repository.performFirstSync(empId)
        
        // Then
        assertTrue(result.isFailure)
        assertEquals(error, result.exceptionOrNull())
    }

    @Test
    fun `getTerritoryItems success should return data`() = runTest {
        // Given
        val empId = "EMP001"
        val territories = listOf(mockk(), mockk())
        coEvery { remoteDataSource.getTerritoryItems(empId) } returns territories
        
        // When
        val result = repository.getTerritoryItems(empId)
        
        // Then
        assertTrue(result.isSuccess)
        assertEquals(territories, result.getOrNull())
    }

    @Test
    fun `getDashboardSummary should return local data`() = runTest {
        // Given
        val summary = mockk<DashboardSummary>()
        coEvery { localDataSource.getDashboardSummary() } returns summary
        
        // When
        val result = repository.getDashboardSummary()
        
        // Then
        assertEquals(summary, result)
        coVerify { localDataSource.getDashboardSummary() }
    }

    @Test
    fun `saveDashboardSummary should save to local storage`() = runTest {
        // Given
        val summary = mockk<DashboardSummary>()
        
        // When
        repository.saveDashboardSummary(summary)
        
        // Then
        coVerify { localDataSource.saveDashboardSummary(summary) }
    }

    @Test
    fun `isFirstSyncDone should return preference value`() = runTest {
        // Given
        coEvery { userPreferencesDataSource.isFirstSyncDone() } returns true
        
        // When
        val result = repository.isFirstSyncDone()
        
        // Then
        assertTrue(result)
        coVerify { userPreferencesDataSource.isFirstSyncDone() }
    }

    @Test
    fun `deleteSyncData should call local data source`() = runTest {
        // When
        repository.deleteSyncData()
        
        // Then
        coVerify { localDataSource.deleteSyncData() }
    }

    @Test
    fun `deviceLogout should call local data source`() = runTest {
        // When
        repository.deviceLogout()
        
        // Then
        coVerify { localDataSource.clearLoginState() }
    }

    @Test
    fun `deleteUserData should call remote data source`() = runTest {
        // Given
        val empId = "EMP001"
        coEvery { remoteDataSource.deleteUserData(empId) } returns Unit
        
        // When
        val result = repository.deleteUserData(empId)
        
        // Then
        assertTrue(result.isSuccess)
        coVerify { remoteDataSource.deleteUserData(empId) }
    }
}
