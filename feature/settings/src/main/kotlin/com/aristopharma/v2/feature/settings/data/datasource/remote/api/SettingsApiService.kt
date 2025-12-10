package com.aristopharma.v2.feature.settings.data.datasource.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

/**
 * Retrofit API service for settings feature.
 * TODO: Replace placeholder endpoints with actual API endpoints when available.
 */
interface SettingsApiService {
    /**
     * Placeholder endpoint for getting user settings.
     * TODO: Replace with actual endpoint when available.
     */
    @GET("api/v1/settings")
    suspend fun getSettings(
        @Query("userId") userId: String,
    ): Response<SettingsDto>

    /**
     * Placeholder endpoint for updating dark theme configuration.
     * TODO: Replace with actual endpoint when available.
     */
    @PUT("api/v1/settings/dark-theme")
    suspend fun updateDarkThemeConfig(
        @Body request: DarkThemeConfigRequest,
    ): Response<Unit>

    /**
     * Placeholder endpoint for updating dynamic color preference.
     * TODO: Replace with actual endpoint when available.
     */
    @PUT("api/v1/settings/dynamic-color")
    suspend fun updateDynamicColorPreference(
        @Body request: DynamicColorRequest,
    ): Response<Unit>

    /**
     * Placeholder endpoint for sign out.
     * TODO: Replace with actual endpoint when available.
     */
    @POST("api/v1/settings/signout")
    suspend fun signOut(): Response<Unit>
}

/**
 * Placeholder DTO for settings.
 * TODO: Replace with actual DTO when available.
 */
data class SettingsDto(
    val userId: String,
    val darkThemeConfig: String,
    val useDynamicColor: Boolean,
    val language: String,
)

/**
 * Placeholder request DTO for dark theme config.
 * TODO: Replace with actual DTO when available.
 */
data class DarkThemeConfigRequest(
    val userId: String,
    val darkThemeConfig: String,
)

/**
 * Placeholder request DTO for dynamic color.
 * TODO: Replace with actual DTO when available.
 */
data class DynamicColorRequest(
    val userId: String,
    val useDynamicColor: Boolean,
)

