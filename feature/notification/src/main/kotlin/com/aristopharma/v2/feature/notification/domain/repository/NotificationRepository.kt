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

package com.aristopharma.v2.feature.notification.domain.repository

import com.aristopharma.v2.feature.notification.data.model.NoticeModel

/**
 * Interface defining notification-related operations.
 */
interface NotificationRepository {
    /**
     * Get notice list for an employee.
     *
     * @param empId The employee ID.
     * @return A [Result] representing the operation result. It contains a list of [NoticeModel] if
     * successful, or an error if there was a problem.
     */
    suspend fun getNoticeList(empId: String): Result<List<NoticeModel>>
}

