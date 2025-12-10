package com.aristopharma.v2.feature.auth.data.datasource.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit API service for auth feature.
 * TODO: Replace placeholder endpoints with actual API endpoints when available.
 */
interface AuthApiService {
    /**
     * Placeholder endpoint for sign in with email and password.
     * TODO: Replace with actual endpoint when available.
     */
    @POST("api/v1/auth/signin")
    suspend fun signIn(
        @Body request: AuthSignInRequest,
    ): Response<AuthSignInResponse>

    /**
     * Placeholder endpoint for sign up with email and password.
     * TODO: Replace with actual endpoint when available.
     */
    @POST("api/v1/auth/signup")
    suspend fun signUp(
        @Body request: AuthSignUpRequest,
    ): Response<AuthSignUpResponse>

    /**
     * Placeholder endpoint for sign out.
     * TODO: Replace with actual endpoint when available.
     */
    @POST("api/v1/auth/signout")
    suspend fun signOut(): Response<Unit>
}

/**
 * Placeholder request DTO for sign in.
 * TODO: Replace with actual DTO when available.
 */
data class AuthSignInRequest(
    val email: String,
    val password: String,
)

/**
 * Placeholder response DTO for sign in.
 * TODO: Replace with actual DTO when available.
 */
data class AuthSignInResponse(
    val token: String,
    val userId: String,
)

/**
 * Placeholder request DTO for sign up.
 * TODO: Replace with actual DTO when available.
 */
data class AuthSignUpRequest(
    val name: String,
    val email: String,
    val password: String,
)

/**
 * Placeholder response DTO for sign up.
 * TODO: Replace with actual DTO when available.
 */
data class AuthSignUpResponse(
    val token: String,
    val userId: String,
)

