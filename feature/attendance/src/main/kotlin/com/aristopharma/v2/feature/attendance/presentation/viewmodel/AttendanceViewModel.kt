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

package com.aristopharma.v2.feature.attendance.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aristopharma.v2.core.utils.OneTimeEvent
import com.aristopharma.v2.core.preferences.data.UserPreferencesDataSource
import com.aristopharma.v2.feature.attendance.data.model.AttendanceRecord
import com.aristopharma.v2.feature.attendance.data.model.AttendanceStatus
import com.aristopharma.v2.feature.attendance.data.model.LocationData
import com.aristopharma.v2.feature.attendance.domain.model.AttendanceEvent
import com.aristopharma.v2.feature.attendance.domain.repository.AttendanceRepository
import com.aristopharma.v2.feature.dashboard.domain.repository.DashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for attendance operations.
 */
@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val dashboardRepository: DashboardRepository,
    private val userPreferencesDataSource: UserPreferencesDataSource
) : ViewModel() {
    
    private val _attendanceState = MutableStateFlow(AttendanceState())
    val attendanceState: StateFlow<AttendanceState> = _attendanceState.asStateFlow()
    
    init {
        loadTodayAttendance()
        loadAttendanceHistory()
    }
    
    fun onEvent(event: AttendanceEvent) {
        when (event) {
            is AttendanceEvent.CheckIn -> checkIn()
            is AttendanceEvent.CheckOut -> checkOut()
            is AttendanceEvent.LoadHistory -> loadAttendanceHistory()
        }
    }
    
    private fun loadTodayAttendance() {
        viewModelScope.launch {
            try {
                val empId = userPreferencesDataSource.getUserIdOrThrow()
                val todayAttendance = attendanceRepository.getTodayAttendance(empId)
                
                _attendanceState.update {
                    it.copy(
                        todayAttendance = todayAttendance,
                        currentStatus = when (todayAttendance?.status) {
                            "CHECKED_IN" -> AttendanceStatus.CHECKED_IN
                            "CHECKED_OUT" -> AttendanceStatus.CHECKED_OUT
                            else -> AttendanceStatus.IDLE
                        }
                    )
                }
            } catch (e: Exception) {
                _attendanceState.update {
                    it.copy(error = OneTimeEvent(e))
                }
            }
        }
    }
    
    private fun loadAttendanceHistory() {
        viewModelScope.launch {
            try {
                val empId = userPreferencesDataSource.getUserIdOrThrow()
                attendanceRepository.getAttendanceHistory(empId).collect { history ->
                    _attendanceState.update {
                        it.copy(attendanceHistory = history)
                    }
                }
            } catch (e: Exception) {
                _attendanceState.update {
                    it.copy(error = OneTimeEvent(e))
                }
            }
        }
    }
    
    private fun checkIn() {
        viewModelScope.launch {
            _attendanceState.update { it.copy(isLoading = true) }
            
            try {
                val empId = userPreferencesDataSource.getUserIdOrThrow()
                
                // TODO: Get actual GPS location
                val location = LocationData(
                    latitude = 23.8103,
                    longitude = 90.4125,
                    accuracy = 10f,
                    timestamp = System.currentTimeMillis()
                )
                
                val result = attendanceRepository.checkIn(empId, location)
                
                result.onSuccess { record ->
                    _attendanceState.update {
                        it.copy(
                            todayAttendance = record,
                            currentStatus = AttendanceStatus.CHECKED_IN,
                            isLoading = false
                        )
                    }
                    
                    // Update dashboard status
                    updateDashboardStatus(AttendanceStatus.CHECKED_IN)
                }.onFailure { error ->
                    _attendanceState.update {
                        it.copy(
                            isLoading = false,
                            error = OneTimeEvent(error)
                        )
                    }
                }
            } catch (e: Exception) {
                _attendanceState.update {
                    it.copy(
                        isLoading = false,
                        error = OneTimeEvent(e)
                    )
                }
            }
        }
    }
    
    private fun checkOut() {
        viewModelScope.launch {
            _attendanceState.update { it.copy(isLoading = true) }
            
            try {
                val empId = userPreferencesDataSource.getUserIdOrThrow()
                val todayAttendance = _attendanceState.value.todayAttendance
                
                if (todayAttendance == null) {
                    _attendanceState.update {
                        it.copy(
                            isLoading = false,
                            error = OneTimeEvent(Exception("No check-in record found"))
                        )
                    }
                    return@launch
                }
                
                val result = attendanceRepository.checkOut(empId, todayAttendance.id)
                
                result.onSuccess { record ->
                    _attendanceState.update {
                        it.copy(
                            todayAttendance = record,
                            currentStatus = AttendanceStatus.CHECKED_OUT,
                            isLoading = false
                        )
                    }
                    
                    // Update dashboard status
                    updateDashboardStatus(AttendanceStatus.CHECKED_OUT)
                }.onFailure { error ->
                    _attendanceState.update {
                        it.copy(
                            isLoading = false,
                            error = OneTimeEvent(error)
                        )
                    }
                }
            } catch (e: Exception) {
                _attendanceState.update {
                    it.copy(
                        isLoading = false,
                        error = OneTimeEvent(e)
                    )
                }
            }
        }
    }
    
    private suspend fun updateDashboardStatus(status: AttendanceStatus) {
        try {
            val summary = dashboardRepository.getDashboardSummary()
            summary?.let {
                val updatedSummary = it.copy(
                    attendanceStatus = when (status) {
                        AttendanceStatus.CHECKED_IN -> "CHECKED_IN"
                        AttendanceStatus.CHECKED_OUT -> "CHECKED_OUT"
                        AttendanceStatus.IDLE -> "IDLE"
                    }
                )
                dashboardRepository.saveDashboardSummary(updatedSummary)
            }
        } catch (e: Exception) {
            // Silently fail dashboard update
        }
    }
}

/**
 * State for attendance screen.
 */
data class AttendanceState(
    val currentStatus: AttendanceStatus = AttendanceStatus.IDLE,
    val todayAttendance: AttendanceRecord? = null,
    val attendanceHistory: List<AttendanceRecord> = emptyList(),
    val isLoading: Boolean = false,
    val error: OneTimeEvent<Throwable>? = null
)
