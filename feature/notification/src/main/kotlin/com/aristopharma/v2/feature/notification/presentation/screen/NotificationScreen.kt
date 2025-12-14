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

package com.aristopharma.v2.feature.notification.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aristopharma.v2.core.ui.utils.SnackbarAction
import com.aristopharma.v2.core.ui.utils.StatefulComposable
import com.aristopharma.v2.feature.notification.presentation.viewmodel.NotificationViewModel

/**
 * Notification screen with Material3 Compose UI.
 * Replaces the legacy NotificationActivity and NoticeBoardFragment.
 */
@Composable
fun NotificationScreen(
    onShowSnackbar: suspend (String, SnackbarAction, Throwable?) -> Boolean,
    notificationViewModel: NotificationViewModel = hiltViewModel(),
) {
    val notificationState by notificationViewModel.notificationUiState.collectAsStateWithLifecycle()

    // Load notices on screen start
    LaunchedEffect(Unit) {
        notificationViewModel.loadNotices()
    }

    StatefulComposable(
        state = notificationState,
        onShowSnackbar = onShowSnackbar,
    ) {
        NotificationScreenContent(
            notices = notificationState.data.notices,
            onNoticeClick = notificationViewModel::onNoticeClick,
        )
    }
}

@Composable
private fun NotificationScreenContent(
    notices: List<com.aristopharma.v2.feature.notification.data.model.NoticeModel>,
    onNoticeClick: (com.aristopharma.v2.feature.notification.data.model.NoticeModel) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        if (notices.isEmpty()) {
            EmptyState()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
            ) {
                items(
                    items = notices,
                    key = { notice -> notice.messageTitle + notice.messageDescription },
                ) { notice ->
                    NoticeItem(
                        notice = notice,
                        onClick = { onNoticeClick(notice) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun NoticeItem(
    notice: com.aristopharma.v2.feature.notification.data.model.NoticeModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = notice.messageTitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (notice.messageDescription.isNotEmpty()) {
                Text(
                    text = notice.messageDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center,
    ) {
        Text(
            text = "No notifications available",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

