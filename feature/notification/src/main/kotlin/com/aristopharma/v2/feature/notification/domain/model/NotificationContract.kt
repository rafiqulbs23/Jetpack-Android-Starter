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

package com.aristopharma.v2.feature.notification.domain.model

import androidx.compose.runtime.Immutable
import com.aristopharma.v2.feature.notification.data.model.NoticeModel

/**
 * State for the notification screen.
 */
@Immutable
data class NotificationState(
    val notices: List<NoticeModel> = emptyList(),
    val selectedNotice: NoticeModel? = null,
)

/**
 * Events for the notification screen.
 */
@Immutable
sealed interface NotificationEvent {
    data object LoadNotices : NotificationEvent
    data class NoticeClicked(val notice: NoticeModel) : NotificationEvent
}

