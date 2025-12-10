package com.aristopharma.v2.feature.home.data.datasource.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit API service for home feature.
 * TODO: Replace placeholder endpoints with actual API endpoints when available.
 */
interface HomeApiService {
    /**
     * Placeholder endpoint for getting jetpacks.
     * TODO: Replace with actual endpoint when available.
     */
    @GET("api/v1/home/jetpacks")
    suspend fun getJetpacks(
        @Query("userId") userId: String,
        @Query("lastSynced") lastSynced: Long? = null,
    ): Response<List<JetpackDto>>

    /**
     * Placeholder endpoint for getting a single jetpack.
     * TODO: Replace with actual endpoint when available.
     */
    @GET("api/v1/home/jetpack")
    suspend fun getJetpack(
        @Query("id") id: String,
    ): Response<JetpackDto>

    /**
     * Placeholder endpoint for creating or updating a jetpack.
     * TODO: Replace with actual endpoint when available.
     */
    @POST("api/v1/home/jetpack")
    suspend fun createOrUpdateJetpack(
        @Body jetpack: JetpackDto,
    ): Response<JetpackDto>

    /**
     * Placeholder endpoint for updating a jetpack.
     * TODO: Replace with actual endpoint when available.
     */
    @PUT("api/v1/home/jetpack")
    suspend fun updateJetpack(
        @Body jetpack: JetpackDto,
    ): Response<JetpackDto>

    /**
     * Placeholder endpoint for deleting a jetpack.
     * TODO: Replace with actual endpoint when available.
     */
    @DELETE("api/v1/home/jetpack")
    suspend fun deleteJetpack(
        @Query("id") id: String,
    ): Response<Unit>
}

/**
 * Placeholder DTO for jetpack.
 * TODO: Replace with actual DTO when available.
 */
data class JetpackDto(
    val id: String,
    val name: String,
    val price: Double,
    val userId: String? = null,
    val lastUpdated: Long? = null,
    val lastSynced: Long? = null,
)

