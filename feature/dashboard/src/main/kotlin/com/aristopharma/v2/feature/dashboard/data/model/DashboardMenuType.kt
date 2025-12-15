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

package com.aristopharma.v2.feature.dashboard.data.model

/**
 * Enum representing different dashboard menu item types.
 * Used for navigation and menu item identification.
 */
enum class DashboardMenuType {
    // Order Management
    DRAFT_ORDER,
    POST_ORDER,
    POST_SPECIAL_ORDER,
    ORDER_HISTORY_USER,
    ORDER_HISTORY_MANAGER,

    // Attendance
    START_YOUR_DAY,
    MANAGER_LIVE_LOCATION,
    ATTENDANCE_REPORT,

    // Leave Management
    LEAVE_MANAGEMENT,
    LEAVE,

    // Reports
    SALES_SUMMARY_REPORT,
    PRODUCT_SALES_REPORT,
    CHEMIST_SALES_REPORT,
}

