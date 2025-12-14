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

package com.aristopharma.v2.feature.notification.data.utils

import com.aristopharma.v2.core.utils.suspendRunCatching
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager for FCM token operations.
 * 
 * Handles FCM token retrieval and management for push notifications.
 */
@Singleton
class FcmTokenManager @Inject constructor() {
    /**
     * Retrieves FCM token using suspend function.
     * 
     * @return The FCM token as a String, or null if retrieval fails.
     */
    suspend fun getFCMToken(): String? = withContext(Dispatchers.IO) {
        suspendRunCatching {
            FirebaseMessaging.getInstance().token.await()
        }.getOrNull()
    }

    /**
     * Retrieves FCM token with callback (for non-suspend contexts).
     * 
     * @param onSuccess Callback invoked with the token when successful.
     * @param onFailure Callback invoked with the exception when failed.
     */
    fun getFCMToken(
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.let(onSuccess) ?: onFailure(
                        IllegalStateException("FCM token is null"),
                    )
                } else {
                    onFailure(
                        task.exception ?: Exception("Failed to get FCM token"),
                    )
                }
            }
    }
}

