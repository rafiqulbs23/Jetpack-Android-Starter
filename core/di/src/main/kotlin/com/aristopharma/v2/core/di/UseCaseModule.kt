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

package com.aristopharma.v2.core.di

import com.aristopharma.v2.feature.home.domain.repository.HomeRepository
import com.aristopharma.v2.feature.home.domain.usecase.CreateOrUpdateJetpackUseCase
import com.aristopharma.v2.feature.home.domain.usecase.DeleteJetpackUseCase
import com.aristopharma.v2.feature.home.domain.usecase.GetJetpackUseCase
import com.aristopharma.v2.feature.home.domain.usecase.GetJetpacksUseCase
import com.aristopharma.v2.feature.auth.domain.repository.AuthRepository
import com.aristopharma.v2.feature.auth.domain.usecase.RegisterWithEmailAndPasswordUseCase
import com.aristopharma.v2.feature.auth.domain.usecase.RegisterWithGoogleUseCase
import com.aristopharma.v2.feature.auth.domain.usecase.SignInWithEmailAndPasswordUseCase
import com.aristopharma.v2.feature.auth.domain.usecase.SignInWithGoogleUseCase
import com.aristopharma.v2.feature.auth.domain.usecase.SignInWithSavedCredentialsUseCase
import com.aristopharma.v2.feature.profile.domain.repository.ProfileRepository
import com.aristopharma.v2.feature.profile.domain.usecase.GetProfileUseCase
import com.aristopharma.v2.feature.profile.domain.usecase.SignOutProfileUseCase
import com.aristopharma.v2.feature.settings.domain.repository.SettingsRepository
import com.aristopharma.v2.feature.settings.domain.usecase.GetSettingsUseCase
import com.aristopharma.v2.feature.settings.domain.usecase.SetDarkThemeConfigUseCase
import com.aristopharma.v2.feature.settings.domain.usecase.SetDynamicColorPreferenceUseCase
import com.aristopharma.v2.feature.settings.domain.usecase.SignOutSettingsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger module that provides use case instances for features.
 *
 * Today only Home use cases exist; extend this module as new feature
 * use cases are added.
 */
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideGetJetpacksUseCase(
        homeRepository: HomeRepository,
    ): GetJetpacksUseCase = GetJetpacksUseCase(homeRepository)

    @Provides
    @Singleton
    fun provideGetJetpackUseCase(
        homeRepository: HomeRepository,
    ): GetJetpackUseCase = GetJetpackUseCase(homeRepository)

    @Provides
    @Singleton
    fun provideCreateOrUpdateJetpackUseCase(
        homeRepository: HomeRepository,
    ): CreateOrUpdateJetpackUseCase = CreateOrUpdateJetpackUseCase(homeRepository)

    @Provides
    @Singleton
    fun provideDeleteJetpackUseCase(
        homeRepository: HomeRepository,
    ): DeleteJetpackUseCase = DeleteJetpackUseCase(homeRepository)

    @Provides
    @Singleton
    fun provideSignInWithSavedCredentialsUseCase(
        authRepository: AuthRepository,
    ): SignInWithSavedCredentialsUseCase = SignInWithSavedCredentialsUseCase(authRepository)

    @Provides
    @Singleton
    fun provideSignInWithEmailAndPasswordUseCase(
        authRepository: AuthRepository,
    ): SignInWithEmailAndPasswordUseCase = SignInWithEmailAndPasswordUseCase(authRepository)

    @Provides
    @Singleton
    fun provideRegisterWithEmailAndPasswordUseCase(
        authRepository: AuthRepository,
    ): RegisterWithEmailAndPasswordUseCase = RegisterWithEmailAndPasswordUseCase(authRepository)

    @Provides
    @Singleton
    fun provideSignInWithGoogleUseCase(
        authRepository: AuthRepository,
    ): SignInWithGoogleUseCase = SignInWithGoogleUseCase(authRepository)

    @Provides
    @Singleton
    fun provideRegisterWithGoogleUseCase(
        authRepository: AuthRepository,
    ): RegisterWithGoogleUseCase = RegisterWithGoogleUseCase(authRepository)

    @Provides
    @Singleton
    fun provideGetProfileUseCase(
        profileRepository: ProfileRepository,
    ): GetProfileUseCase = GetProfileUseCase(profileRepository)

    @Provides
    @Singleton
    fun provideSignOutProfileUseCase(
        profileRepository: ProfileRepository,
    ): SignOutProfileUseCase = SignOutProfileUseCase(profileRepository)

    @Provides
    @Singleton
    fun provideGetSettingsUseCase(
        settingsRepository: SettingsRepository,
    ): GetSettingsUseCase = GetSettingsUseCase(settingsRepository)

    @Provides
    @Singleton
    fun provideSetDarkThemeConfigUseCase(
        settingsRepository: SettingsRepository,
    ): SetDarkThemeConfigUseCase = SetDarkThemeConfigUseCase(settingsRepository)

    @Provides
    @Singleton
    fun provideSetDynamicColorPreferenceUseCase(
        settingsRepository: SettingsRepository,
    ): SetDynamicColorPreferenceUseCase = SetDynamicColorPreferenceUseCase(settingsRepository)

    @Provides
    @Singleton
    fun provideSignOutSettingsUseCase(
        settingsRepository: SettingsRepository,
    ): SignOutSettingsUseCase = SignOutSettingsUseCase(settingsRepository)
}

