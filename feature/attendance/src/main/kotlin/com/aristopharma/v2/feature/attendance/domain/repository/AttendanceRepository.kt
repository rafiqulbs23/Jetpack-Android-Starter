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

package com.aristopharma.v2.feature.attendance.domain.repository

import com.aristopharma.v2.feature.attendance.data.model.AttendanceRecord
import com.aristopharma.v2.feature.attendance.data.model.LocationData
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for attendance operations.
 */
interface AttendanceRepository {
    suspend fun checkIn(empId: String, location: LocationData): Result<AttendanceRecord>
    suspend fun checkOut(empId: String, attendanceId: String): Result<AttendanceRecord>
    fun getAttendanceHistory(empId: String): Flow<List<AttendanceRecord>>
    suspend fun getTodayAttendance(empId: String): AttendanceRecord?
}
