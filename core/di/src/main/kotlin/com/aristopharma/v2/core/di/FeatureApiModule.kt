package com.aristopharma.v2.core.di

import com.aristopharma.v2.feature.auth.data.datasource.remote.AuthApiService
import com.aristopharma.v2.feature.dashboard.data.datasource.remote.api.DashboardApiService
import com.aristopharma.v2.feature.home.data.datasource.remote.api.HomeApiService
import com.aristopharma.v2.feature.notification.data.datasource.remote.NotificationApiService
import com.aristopharma.v2.feature.profile.data.datasource.remote.api.ProfileApiService
import com.aristopharma.v2.feature.settings.data.datasource.remote.api.SettingsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

/**
 * Provides Retrofit API services for each feature.
 * Endpoints are placeholders; define interfaces when available.
 */
@Module
@InstallIn(SingletonComponent::class)
object FeatureApiModule {
    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApiService =
        retrofit.create(AuthApiService::class.java)

    @Provides
    @Singleton
    fun provideHomeApi(retrofit: Retrofit): HomeApiService =
        retrofit.create(HomeApiService::class.java)

    @Provides
    @Singleton
    fun provideProfileApi(retrofit: Retrofit): ProfileApiService =
        retrofit.create(ProfileApiService::class.java)

    @Provides
    @Singleton
    fun provideSettingsApi(retrofit: Retrofit): SettingsApiService =
        retrofit.create(SettingsApiService::class.java)

    @Provides
    @Singleton
    fun provideNotificationApi(retrofit: Retrofit): NotificationApiService =
        retrofit.create(NotificationApiService::class.java)

    @Provides
    @Singleton
    fun provideDashboardApi(retrofit: Retrofit): DashboardApiService =
        retrofit.create(DashboardApiService::class.java)
}


