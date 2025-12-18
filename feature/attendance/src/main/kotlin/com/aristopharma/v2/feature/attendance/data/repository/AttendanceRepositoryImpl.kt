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

package com.aristopharma.v2.feature.attendance.data.repository

import com.aristopharma.v2.feature.attendance.data.datasource.local.AttendanceLocalDataSource
import com.aristopharma.v2.feature.attendance.data.datasource.remote.AttendanceRemoteDataSource
import com.aristopharma.v2.feature.attendance.data.model.AttendanceRecord
import com.aristopharma.v2.feature.attendance.data.model.CheckInRequest
import com.aristopharma.v2.feature.attendance.data.model.CheckOutRequest
import com.aristopharma.v2.feature.attendance.data.model.LocationData
import com.aristopharma.v2.feature.attendance.domain.repository.AttendanceRepository
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

/**
 * Implementation of AttendanceRepository.
 */
class AttendanceRepositoryImpl @Inject constructor(
    private val remoteDataSource: AttendanceRemoteDataSource,
    private val localDataSource: AttendanceLocalDataSource
) : AttendanceRepository {
    
    override suspend fun checkIn(empId: String, location: LocationData): Result<AttendanceRecord> {
        return try {
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            val request = CheckInRequest(
                empId = empId,
                latitude = location.latitude,
                longitude = location.longitude,
                timestamp = timestamp
            )
            
            val record = remoteDataSource.checkIn(request)
            
            if (record != null) {
                localDataSource.insertAttendance(record)
                Result.success(record)
            } else {
                // Create local record if API fails
                val localRecord = AttendanceRecord(
                    id = UUID.randomUUID().toString(),
                    empId = empId,
                    checkInTime = timestamp,
                    checkOutTime = null,
                    latitude = location.latitude,
                    longitude = location.longitude,
                    status = "CHECKED_IN",
                    date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                )
                localDataSource.insertAttendance(localRecord)
                Result.success(localRecord)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun checkOut(empId: String, attendanceId: String): Result<AttendanceRecord> {
        return try {
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            val request = CheckOutRequest(
                empId = empId,
                attendanceId = attendanceId,
                timestamp = timestamp
            )
            
            val record = remoteDataSource.checkOut(request)
            
            if (record != null) {
                localDataSource.updateCheckOut(attendanceId, timestamp, "CHECKED_OUT")
                Result.success(record)
            } else {
                // Update local record if API fails
                localDataSource.updateCheckOut(attendanceId, timestamp, "CHECKED_OUT")
                val updatedRecord = localDataSource.getTodayAttendance(
                    empId,
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                )
                updatedRecord?.let { Result.success(it) } ?: Result.failure(Exception("Record not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun getAttendanceHistory(empId: String): Flow<List<AttendanceRecord>> {
        return localDataSource.getAttendanceHistory(empId)
    }
    
    override suspend fun getTodayAttendance(empId: String): AttendanceRecord? {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        return localDataSource.getTodayAttendance(empId, today)
    }
}
