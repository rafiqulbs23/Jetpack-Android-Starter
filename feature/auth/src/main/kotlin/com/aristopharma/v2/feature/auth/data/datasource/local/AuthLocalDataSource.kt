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

package com.aristopharma.v2.feature.auth.data.datasource.local

import androidx.datastore.core.DataStore
import com.aristopharma.v2.core.di.IoDispatcher
import com.aristopharma.v2.core.preferences.data.UserPreferencesDataSource
import com.aristopharma.v2.core.preferences.model.PreferencesUserProfile
import com.aristopharma.v2.feature.auth.data.model.LoginModel
import com.aristopharma.v2.firebase.auth.model.AuthUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Local data source for authentication-related data storage.
 *
 * @param userPreferencesDataSource The user preferences data source.
 * @param loginModelDataStore The DataStore for storing login model.
 * @param ioDispatcher The CoroutineDispatcher for performing I/O operations.
 */
class AuthLocalDataSource @Inject constructor(
    private val userPreferencesDataSource: UserPreferencesDataSource,
    private val loginModelDataStore: DataStore<LoginModel>,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    /**
     * Save user profile to local storage.
     *
     * @param user The authenticated user to save.
     */
    suspend fun saveUserProfile(user: AuthUser) {
        userPreferencesDataSource.setUserProfile(user.asPreferencesUserProfile())
    }

    /**
     * Save login model to local storage.
     *
     * @param loginModel The login model to save.
     */
    suspend fun saveLoginModel(loginModel: LoginModel) {
        withContext(ioDispatcher) {
            loginModelDataStore.updateData { loginModel }
        }
    }

    /**
     * Get saved login model from local storage.
     *
     * @return The saved login model, or null if not found.
     */
    suspend fun getLoginModel(): LoginModel? {
        return withContext(ioDispatcher) {
            try {
                loginModelDataStore.data.first()
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * Get login model as a Flow for reactive updates.
     *
     * @return A Flow of [LoginModel].
     */
    fun getLoginModelFlow(): Flow<LoginModel> {
        return loginModelDataStore.data
    }

    /**
     * Clear saved login model.
     */
    suspend fun clearLoginModel() {
        withContext(ioDispatcher) {
            loginModelDataStore.updateData { LoginModel() }
        }
    }

    /**
     * Check if user is logged in.
     *
     * @return true if user is logged in, false otherwise.
     */
    suspend fun isLoggedIn(): Boolean {
        return withContext(ioDispatcher) {
            try {
                loginModelDataStore.data.first().isLoggedIn
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * Reset user preferences (used on sign out).
     */
    suspend fun resetUserPreferences() {
        userPreferencesDataSource.resetUserPreferences()
    }

    /**
     * Convert an [AuthUser] to a [PreferencesUserProfile].
     */
    private fun AuthUser.asPreferencesUserProfile() = PreferencesUserProfile(
        id = id,
        userName = name,
        profilePictureUriString = profilePictureUri?.toString(),
    )
}


