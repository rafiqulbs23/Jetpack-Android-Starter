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

package com.aristopharma.v2.feature.notification.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aristopharma.v2.core.preferences.data.UserPreferencesDataSource
import com.aristopharma.v2.core.ui.utils.UiState
import com.aristopharma.v2.core.ui.utils.updateState
import com.aristopharma.v2.core.utils.OneTimeEvent
import com.aristopharma.v2.feature.notification.domain.model.NotificationState
import com.aristopharma.v2.feature.notification.domain.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for notification feature.
 *
 * Handles fetching and displaying notifications/notices.
 */
@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val userPreferencesDataSource: UserPreferencesDataSource,
) : ViewModel() {

    private val _notificationUiState = MutableStateFlow(UiState(NotificationState()))
    val notificationUiState = _notificationUiState.asStateFlow()

    /**
     * Loads notice list for the current user.
     */
    fun loadNotices() {
        viewModelScope.launch {
            _notificationUiState.update { it.copy(loading = true) }

            val empId = try {
                userPreferencesDataSource.getUserIdOrThrow()
            } catch (e: Exception) {
                _notificationUiState.update {
                    it.copy(
                        loading = false,
                        error = OneTimeEvent(Exception("Employee ID not found", e)),
                    )
                }
                return@launch
            }

            val result = notificationRepository.getNoticeList(empId)

            result.fold(
                onSuccess = { notices ->
                    _notificationUiState.updateState {
                        copy(notices = notices)
                    }
                    _notificationUiState.update { it.copy(loading = false) }
                },
                onFailure = { error ->
                    _notificationUiState.update {
                        it.copy(
                            loading = false,
                            error = OneTimeEvent(error),
                        )
                    }
                },
            )
        }
    }

    /**
     * Handles notice click event.
     *
     * @param notice The clicked notice.
     */
    fun onNoticeClick(notice: com.aristopharma.v2.feature.notification.data.model.NoticeModel) {
        _notificationUiState.updateState {
            copy(selectedNotice = notice)
        }
    }

    /**
     * Clears the selected notice.
     */
    fun clearSelectedNotice() {
        _notificationUiState.updateState {
            copy(selectedNotice = null)
        }
    }
}

