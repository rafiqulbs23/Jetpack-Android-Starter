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

package com.aristopharma.v2.feature.notification.data.datasource.remote

import com.aristopharma.v2.core.ui.utils.BaseResponse
import com.aristopharma.v2.feature.notification.data.model.NoticeModel

/**
 * Remote data source interface for notification operations.
 */
interface NotificationRemoteDataSource {
    /**
     * Get notice list from remote API.
     *
     * @param empId The employee ID.
     * @return A [BaseResponse] containing a list of [NoticeModel] or null if request fails.
     */
    suspend fun getNoticeList(empId: String): BaseResponse<List<NoticeModel>?>?
}

