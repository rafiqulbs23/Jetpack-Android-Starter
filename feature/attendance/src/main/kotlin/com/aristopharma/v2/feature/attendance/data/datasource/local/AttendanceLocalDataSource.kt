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

package com.aristopharma.v2.feature.attendance.data.datasource.local

import com.aristopharma.v2.feature.attendance.data.local.dao.AttendanceDao
import com.aristopharma.v2.feature.attendance.data.local.mapper.toEntity
import com.aristopharma.v2.feature.attendance.data.local.mapper.toModel
import com.aristopharma.v2.feature.attendance.data.model.AttendanceRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Local data source for attendance operations using Room database.
 */
class AttendanceLocalDataSource @Inject constructor(
    private val attendanceDao: AttendanceDao
) {
    fun getAttendanceHistory(empId: String): Flow<List<AttendanceRecord>> {
        return attendanceDao.getAttendanceHistory(empId)
            .map { entities -> entities.map { it.toModel() } }
    }
    
    suspend fun getTodayAttendance(empId: String, date: String): AttendanceRecord? {
        return attendanceDao.getTodayAttendance(empId, date)?.toModel()
    }
    
    suspend fun insertAttendance(attendance: AttendanceRecord) {
        attendanceDao.insertAttendance(attendance.toEntity())
    }
    
    suspend fun updateCheckOut(id: String, checkOutTime: String, status: String) {
        attendanceDao.updateCheckOut(id, checkOutTime, status)
    }
    
    suspend fun deleteAttendanceByEmpId(empId: String) {
        attendanceDao.deleteAttendanceByEmpId(empId)
    }
}
