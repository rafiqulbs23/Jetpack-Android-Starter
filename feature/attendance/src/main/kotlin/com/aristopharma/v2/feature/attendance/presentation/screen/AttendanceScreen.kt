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

package com.aristopharma.v2.feature.attendance.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aristopharma.v2.feature.attendance.data.model.AttendanceStatus
import com.aristopharma.v2.feature.attendance.domain.model.AttendanceEvent
import com.aristopharma.v2.feature.attendance.presentation.viewmodel.AttendanceViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Attendance screen for check-in/check-out.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    onNavigateBack: () -> Unit,
    viewModel: AttendanceViewModel = hiltViewModel()
) {
    val state by viewModel.attendanceState.collectAsStateWithLifecycle()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Attendance") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = when (state.currentStatus) {
                        AttendanceStatus.CHECKED_IN -> MaterialTheme.colorScheme.primaryContainer
                        AttendanceStatus.CHECKED_OUT -> MaterialTheme.colorScheme.errorContainer
                        AttendanceStatus.IDLE -> MaterialTheme.colorScheme.surfaceVariant
                    }
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Current Status",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = when (state.currentStatus) {
                            AttendanceStatus.CHECKED_IN -> "● Checked In"
                            AttendanceStatus.CHECKED_OUT -> "● Checked Out"
                            AttendanceStatus.IDLE -> "● Idle"
                        },
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    state.todayAttendance?.let { attendance ->
                        Text(
                            text = "Check-in: ${attendance.checkInTime}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        attendance.checkOutTime?.let {
                            Text(
                                text = "Check-out: $it",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
            
            // Action Button
            Button(
                onClick = {
                    when (state.currentStatus) {
                        AttendanceStatus.IDLE, AttendanceStatus.CHECKED_OUT -> {
                            viewModel.onEvent(AttendanceEvent.CheckIn)
                        }
                        AttendanceStatus.CHECKED_IN -> {
                            viewModel.onEvent(AttendanceEvent.CheckOut)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = when (state.currentStatus) {
                            AttendanceStatus.IDLE, AttendanceStatus.CHECKED_OUT -> "Check In"
                            AttendanceStatus.CHECKED_IN -> "Check Out"
                        }
                    )
                }
            }
            
            // History Section
            Text(
                text = "Attendance History",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            if (state.attendanceHistory.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No attendance history",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.attendanceHistory) { attendance ->
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = attendance.date,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "In: ${attendance.checkInTime}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                attendance.checkOutTime?.let {
                                    Text(
                                        text = "Out: $it",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    // Error handling
    state.error?.getContentIfNotHandled()?.let { error ->
        LaunchedEffect(error) {
            // TODO: Show snackbar with error message
        }
    }
}
