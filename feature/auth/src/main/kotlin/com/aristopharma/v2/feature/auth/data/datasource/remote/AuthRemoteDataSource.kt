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

package com.aristopharma.v2.feature.auth.data.datasource.remote

import android.app.Activity
import com.aristopharma.v2.feature.auth.data.datasource.remote.api.AuthApiService
import com.aristopharma.v2.feature.auth.data.datasource.remote.api.AuthSignInRequest
import com.aristopharma.v2.feature.auth.data.datasource.remote.api.AuthSignUpRequest
import com.aristopharma.v2.firebase.auth.model.AuthUser
import javax.inject.Inject

/**
 * Remote data source for authentication operations using API services.
 *
 * @param apiService The Retrofit API service for auth endpoints.
 */
class AuthRemoteDataSource @Inject constructor(
    private val apiService: AuthApiService,
) {
    /**
     * Sign in with saved credentials.
     *
     * @param activity The activity instance.
     * @return The authenticated user.
     */
    suspend fun signInWithSavedCredentials(activity: Activity): AuthUser {
        // TODO: Implement when API endpoint is available
        throw NotImplementedError("signInWithSavedCredentials not implemented via API")
    }

    /**
     * Sign in with email and password.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @return The authenticated user.
     */
    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String,
    ): AuthUser {
        val response = apiService.signIn(AuthSignInRequest(email, password))
        if (response.isSuccessful && response.body() != null) {
            val signInResponse = response.body()!!
            // TODO: Map response to AuthUser when DTOs are finalized
            return AuthUser(
                id = signInResponse.userId,
                name = "",
                profilePictureUri = null,
            )
        } else {
            throw Exception("Sign in failed: ${response.message()}")
        }
    }

    /**
     * Register a new user with email and password.
     *
     * @param name The user's name.
     * @param email The user's email address.
     * @param password The user's password.
     * @param activity The activity instance.
     * @return The authenticated user.
     */
    suspend fun registerWithEmailAndPassword(
        name: String,
        email: String,
        password: String,
        activity: Activity,
    ): AuthUser {
        val response = apiService.signUp(AuthSignUpRequest(name, email, password))
        if (response.isSuccessful && response.body() != null) {
            val signUpResponse = response.body()!!
            // TODO: Map response to AuthUser when DTOs are finalized
            return AuthUser(
                id = signUpResponse.userId,
                name = name,
                profilePictureUri = null,
            )
        } else {
            throw Exception("Sign up failed: ${response.message()}")
        }
    }

    /**
     * Sign in with Google.
     *
     * @param activity The current activity.
     * @return The authenticated user.
     */
    suspend fun signInWithGoogle(activity: Activity): AuthUser {
        // TODO: Implement when API endpoint is available
        throw NotImplementedError("signInWithGoogle not implemented via API")
    }

    /**
     * Register a new user with Google.
     *
     * @param activity The current activity.
     * @return The authenticated user.
     */
    suspend fun registerWithGoogle(activity: Activity): AuthUser {
        // TODO: Implement when API endpoint is available
        throw NotImplementedError("registerWithGoogle not implemented via API")
    }

    /**
     * Sign out the current user.
     */
    suspend fun signOut() {
        val response = apiService.signOut()
        if (!response.isSuccessful) {
            throw Exception("Sign out failed: ${response.message()}")
        }
    }
}

