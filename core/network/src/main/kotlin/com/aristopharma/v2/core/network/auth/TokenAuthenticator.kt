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

package com.aristopharma.v2.core.network.auth

import com.aristopharma.v2.core.preferences.data.UserPreferencesDataSource
import com.aristopharma.v2.core.preferences.model.PreferencesUserProfile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Authenticator for refreshing tokens when they expire (401 responses).
 * 
 * This class intercepts 401 Unauthorized responses and attempts to refresh
 * the access token using the refresh token. If successful, it retries the
 * original request with the new token.
 * 
 * Based on implementation from aristopharma-v2 project.
 */
@Singleton
class TokenAuthenticator @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
    // TODO: Inject your refresh token API service here when available
    // private val authApi: AuthApiService
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // Prevent infinite refresh loops - if we already tried to refresh, give up
        if (responseCount(response) >= 3) {
            Timber.w("Token refresh failed after 3 attempts, clearing session")
            clearUserSession()
            return null
        }

        return runBlocking {
            try {
                // Get current tokens from DataStore
                val userData = userPreferencesDataSource.getUserDataPreferences().first()
                val refreshToken = userData.refreshToken

                if (refreshToken.isEmpty()) {
                    Timber.w("No refresh token available, cannot refresh")
                    clearUserSession()
                    return@runBlocking null
                }

                // TODO: Call your refresh token API endpoint
                // Example implementation:
                // val refreshResponse = authApi.refreshToken(
                //     RefreshTokenRequest(refreshToken = refreshToken)
                // )
                //
                // if (refreshResponse.success) {
                //     val newTokens = refreshResponse.data
                //     
                //     // Save new tokens to DataStore
                //     userPreferencesDataSource.setUserProfile(
                //         PreferencesUserProfile(
                //             id = userData.id,
                //             userName = userData.userName,
                //             profilePictureUriString = userData.profilePictureUriString,
                //             accessToken = newTokens.accessToken,
                //             refreshToken = newTokens.refreshToken,
                //             fcmToken = userData.fcmToken
                //         )
                //     )
                //     
                //     // Retry the original request with new token
                //     return@runBlocking response.request.newBuilder()
                //         .header("Authorization", "Bearer ${newTokens.accessToken}")
                //         .build()
                // } else {
                //     Timber.e("Token refresh failed: ${refreshResponse.message}")
                //     clearUserSession()
                //     return@runBlocking null
                // }

                // Temporary: Return null until refresh API is implemented
                Timber.d("Token refresh not implemented yet")
                null

            } catch (e: Exception) {
                Timber.e(e, "Exception during token refresh")
                clearUserSession()
                null
            }
        }
    }

    /**
     * Counts how many times we've tried to refresh the token for this request chain.
     */
    private fun responseCount(response: Response): Int {
        var count = 1
        var currentResponse = response.priorResponse
        while (currentResponse != null) {
            count++
            currentResponse = currentResponse.priorResponse
        }
        return count
    }

    /**
     * Clears the user session when token refresh fails.
     */
    private fun clearUserSession() {
        runBlocking {
            try {
                userPreferencesDataSource.resetUserPreferences()
                Timber.d("User session cleared")
            } catch (e: Exception) {
                Timber.e(e, "Failed to clear user session")
            }
        }
    }
}
