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

package com.aristopharma.v2.feature.dashboard.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aristopharma.v2.feature.dashboard.data.model.DashboardSummary

/**
 * Composable for displaying dashboard summary information.
 *
 * @param summary The dashboard summary data.
 * @param onSyncClick Callback when sync button is clicked.
 * @param modifier Modifier for the composable.
 */
@Composable
fun DashboardSummaryCard(
    summary: DashboardSummary,
    onSyncClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            // Employee Info
            Text(
                text = summary.employeeName.ifEmpty { "Employee" },
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = "ID: ${summary.employeeId}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Attendance Status
            Text(
                text = "Attendance: ${summary.attendanceStatus}",
                style = MaterialTheme.typography.bodyMedium,
                color = getAttendanceColor(summary.attendanceStatus),
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Sync Info
            Text(
                text = "Last Sync: ${summary.getLastSyncRelativeTime()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Text(
                text = summary.getLastSyncDate(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Sync Button
            Button(
                onClick = onSyncClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
            ) {
                Text(if (isLoading) "Syncing..." else "Sync Now")
            }
        }
    }
}

/**
 * Gets the color for attendance status.
 *
 * @param status The attendance status.
 * @return The color for the status.
 */
@Composable
private fun getAttendanceColor(status: String): Color {
    return when (status) {
        DashboardSummary.CHECKED_IN -> Color(0xFF4CAF50) // Green
        DashboardSummary.CHECKED_OUT -> Color(0xFFF44336) // Red
        DashboardSummary.IDLE -> Color(0xFFFF9800) // Orange
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
}

