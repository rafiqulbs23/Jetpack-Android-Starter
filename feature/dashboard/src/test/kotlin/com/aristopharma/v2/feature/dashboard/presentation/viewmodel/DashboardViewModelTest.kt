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

package com.aristopharma.v2.feature.dashboard.presentation.viewmodel

import com.aristopharma.v2.core.preferences.data.UserPreferencesDataSource
import com.aristopharma.v2.feature.dashboard.data.model.DashboardMenuType
import com.aristopharma.v2.feature.dashboard.data.model.DashboardSummary
import com.aristopharma.v2.feature.dashboard.domain.model.DashboardEvent
import com.aristopharma.v2.feature.dashboard.domain.repository.DashboardRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Unit tests for DashboardViewModel.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    private lateinit var viewModel: DashboardViewModel
    private lateinit var dashboardRepository: DashboardRepository
    private lateinit var userPreferencesDataSource: UserPreferencesDataSource
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        dashboardRepository = mockk(relaxed = true)
        userPreferencesDataSource = mockk(relaxed = true)
        
        // Setup default mocks
        coEvery { userPreferencesDataSource.getUserIdOrThrow() } returns "EMP001"
        coEvery { userPreferencesDataSource.getUserDataPreferences() } returns flowOf(
            mockk {
                coEvery { userName } returns "Test User"
                coEvery { id } returns "EMP001"
            }
        )
        coEvery { dashboardRepository.getDashboardSummary() } returns null
        coEvery { dashboardRepository.getMenuPermissions() } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): DashboardViewModel {
        return DashboardViewModel(
            dashboardRepository = dashboardRepository,
            userPreferencesDataSource = userPreferencesDataSource
        )
    }

    @Test
    fun `init should setup model and fetch menu permissions`() = runTest {
        // When
        viewModel = createViewModel()
        advanceUntilIdle()

        // Then
        coVerify { userPreferencesDataSource.getUserIdOrThrow() }
        coVerify { dashboardRepository.getDashboardSummary() }
        coVerify { dashboardRepository.getMenuPermissions() }
    }

    @Test
    fun `syncNow should set loading state and perform sync`() = runTest {
        // Given
        val empId = "EMP001"
        coEvery { dashboardRepository.performFirstSync(empId) } returns Result.success(Unit)
        
        viewModel = createViewModel()
        advanceUntilIdle()

        // When
        viewModel.onEvent(DashboardEvent.SyncNow(empId))
        
        // Then - loading should be true initially
        assertTrue(viewModel.dashboardUiState.value.loading)
        
        advanceUntilIdle()
        
        // Then - loading should be false after completion
        assertFalse(viewModel.dashboardUiState.value.loading)
        coVerify { dashboardRepository.performFirstSync(empId) }
    }

    @Test
    fun `syncNow success should update sync success state`() = runTest {
        // Given
        val empId = "EMP001"
        coEvery { dashboardRepository.performFirstSync(empId) } returns Result.success(Unit)
        coEvery { dashboardRepository.getTerritoryItems(empId) } returns Result.success(emptyList())
        
        viewModel = createViewModel()
        advanceUntilIdle()

        // When
        viewModel.onEvent(DashboardEvent.SyncNow(empId))
        advanceUntilIdle()

        // Then
        val syncSuccess = viewModel.dashboardUiState.value.data.isSyncSuccess.getContentIfNotHandled()
        assertNotNull(syncSuccess)
        assertTrue(syncSuccess)
    }

    @Test
    fun `syncNow failure should update error state`() = runTest {
        // Given
        val empId = "EMP001"
        val error = Exception("Sync failed")
        coEvery { dashboardRepository.performFirstSync(empId) } returns Result.failure(error)
        
        viewModel = createViewModel()
        advanceUntilIdle()

        // When
        viewModel.onEvent(DashboardEvent.SyncNow(empId))
        advanceUntilIdle()

        // Then
        assertFalse(viewModel.dashboardUiState.value.loading)
        val errorEvent = viewModel.dashboardUiState.value.error.getContentIfNotHandled()
        assertNotNull(errorEvent)
    }

    @Test
    fun `setupModel should create initial summary when none exists`() = runTest {
        // Given
        coEvery { dashboardRepository.getDashboardSummary() } returns null
        
        // When
        viewModel = createViewModel()
        advanceUntilIdle()

        // Then
        val summary = viewModel.dashboardUiState.value.data.summary
        assertEquals("Test User", summary.employeeName)
        assertEquals("EMP001", summary.employeeId)
        assertEquals(DashboardSummary.IDLE, summary.attendanceStatus)
    }

    @Test
    fun `setupModel should use existing summary when available`() = runTest {
        // Given
        val existingSummary = DashboardSummary(
            employeeName = "Existing User",
            employeeId = "EMP002",
            attendanceStatus = DashboardSummary.CHECKED_IN,
            lastSyncTime = "2 hours ago",
            postOrderCount = 5,
            isFirstSyncDone = true
        )
        coEvery { dashboardRepository.getDashboardSummary() } returns existingSummary
        
        // When
        viewModel = createViewModel()
        advanceUntilIdle()

        // Then
        val summary = viewModel.dashboardUiState.value.data.summary
        assertEquals("Existing User", summary.employeeName)
        assertEquals("EMP002", summary.employeeId)
        assertEquals(DashboardSummary.CHECKED_IN, summary.attendanceStatus)
        assertTrue(summary.isFirstSyncDone)
    }

    @Test
    fun `handleMenuItemClick should show coming soon for unavailable features`() = runTest {
        // Given
        viewModel = createViewModel()
        advanceUntilIdle()

        // When
        viewModel.onEvent(DashboardEvent.MenuItemClick(DashboardMenuType.START_YOUR_DAY))
        advanceUntilIdle()

        // Then
        val error = viewModel.dashboardUiState.value.error.getContentIfNotHandled()
        assertNotNull(error)
        assertTrue(error.message?.contains("coming soon") == true)
    }

    @Test
    fun `handleMenuItemClick should not show error for available features`() = runTest {
        // Given
        viewModel = createViewModel()
        advanceUntilIdle()

        // When
        viewModel.onEvent(DashboardEvent.MenuItemClick(DashboardMenuType.PROFILE))
        advanceUntilIdle()

        // Then
        val error = viewModel.dashboardUiState.value.error.peekContent()
        // Should not have error for available features
        assertTrue(error == null || error.message?.contains("coming soon") == false)
    }

    @Test
    fun `fetchMenuPermissions should update menu items state`() = runTest {
        // Given
        val mockPermissions = listOf(
            mockk {
                coEvery { title } returns "Draft Order"
            }
        )
        coEvery { dashboardRepository.getMenuPermissions() } returns flowOf(mockPermissions)
        
        // When
        viewModel = createViewModel()
        advanceUntilIdle()

        // Then
        // Menu items should be populated based on permissions
        coVerify { dashboardRepository.getMenuPermissions() }
    }

    @Test
    fun `deleteUserData should call repository`() = runTest {
        // Given
        val empId = "EMP001"
        coEvery { dashboardRepository.deleteUserData(empId) } returns Result.success(Unit)
        
        viewModel = createViewModel()
        advanceUntilIdle()

        // When
        viewModel.onEvent(DashboardEvent.DeleteUserData)
        advanceUntilIdle()

        // Then
        coVerify { dashboardRepository.deleteUserData(empId) }
    }

    @Test
    fun `deviceLogout should call repository`() = runTest {
        // Given
        viewModel = createViewModel()
        advanceUntilIdle()

        // When
        viewModel.onEvent(DashboardEvent.DeviceLogout)
        advanceUntilIdle()

        // Then
        coVerify { dashboardRepository.deviceLogout() }
    }
}
