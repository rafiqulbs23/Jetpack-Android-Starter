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

package com.aristopharma.v2.feature.attendance.data.datasource.remote

import com.aristopharma.v2.core.ui.utils.BaseResponse
import com.aristopharma.v2.feature.attendance.data.model.AttendanceRecord
import com.aristopharma.v2.feature.attendance.data.model.CheckInRequest
import com.aristopharma.v2.feature.attendance.data.model.CheckOutRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * API service for attendance operations.
 */
interface AttendanceApiService {
    
    @POST("attendance/check-in")
    suspend fun checkIn(@Body request: CheckInRequest): BaseResponse<AttendanceRecord>
    
    @POST("attendance/check-out")
    suspend fun checkOut(@Body request: CheckOutRequest): BaseResponse<AttendanceRecord>
    
    @GET("attendance/history/{empId}")
    suspend fun getAttendanceHistory(@Path("empId") empId: String): BaseResponse<List<AttendanceRecord>>
}
