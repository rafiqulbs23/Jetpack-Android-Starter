package com.aristopharma.v2.feature.profile.data.datasource.remote.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Body
import retrofit2.http.Query

/**
 * Retrofit API service for profile feature.
 * TODO: Replace placeholder endpoints with actual API endpoints when available.
 */
interface ProfileApiService {
    /**
     * Placeholder endpoint for getting user profile.
     * TODO: Replace with actual endpoint when available.
     */
    @GET("api/v1/profile")
    suspend fun getProfile(
        @Query("userId") userId: String,
    ): Response<ProfileDto>

    /**
     * Placeholder endpoint for updating user profile.
     * TODO: Replace with actual endpoint when available.
     */
    @PUT("api/v1/profile")
    suspend fun updateProfile(
        @Body profile: ProfileDto,
    ): Response<ProfileDto>

    /**
     * Placeholder endpoint for sign out.
     * TODO: Replace with actual endpoint when available.
     */
    @POST("api/v1/profile/signout")
    suspend fun signOut(): Response<Unit>
}

/**
 * Placeholder DTO for profile.
 * TODO: Replace with actual DTO when available.
 */
data class ProfileDto(
    val userId: String,
    val userName: String,
    val profilePictureUri: String? = null,
)

