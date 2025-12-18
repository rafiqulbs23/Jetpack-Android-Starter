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

package com.aristopharma.v2.feature.attendance.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aristopharma.v2.feature.attendance.data.local.entity.AttendanceEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for attendance operations.
 */
@Dao
interface AttendanceDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(attendance: AttendanceEntity)
    
    @Query("SELECT * FROM attendance_records WHERE empId = :empId ORDER BY checkInTime DESC")
    fun getAttendanceHistory(empId: String): Flow<List<AttendanceEntity>>
    
    @Query("SELECT * FROM attendance_records WHERE empId = :empId AND date = :date LIMIT 1")
    suspend fun getTodayAttendance(empId: String, date: String): AttendanceEntity?
    
    @Query("UPDATE attendance_records SET checkOutTime = :checkOutTime, status = :status WHERE id = :id")
    suspend fun updateCheckOut(id: String, checkOutTime: String, status: String)
    
    @Query("DELETE FROM attendance_records WHERE empId = :empId")
    suspend fun deleteAttendanceByEmpId(empId: String)
    
    @Query("DELETE FROM attendance_records")
    suspend fun deleteAllAttendance()
}
