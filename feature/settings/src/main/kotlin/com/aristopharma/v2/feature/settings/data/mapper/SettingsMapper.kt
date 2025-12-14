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

package com.aristopharma.v2.feature.settings.data.mapper

import com.aristopharma.v2.core.preferences.model.DarkThemeConfigPreferences
import com.aristopharma.v2.core.preferences.model.UserDataPreferences
import com.aristopharma.v2.feature.settings.domain.model.DarkThemeConfig
import com.aristopharma.v2.feature.settings.domain.model.Settings

/**
 * Extension function to map [UserDataPreferences] to [Settings].
 *
 * @return The mapped [Settings].
 */
fun UserDataPreferences.asSettings(): Settings {
    return Settings(
        userName = userName,
        useDynamicColor = useDynamicColor,
        darkThemeConfig = darkThemeConfigPreferences.toDarkThemeConfig(),
    )
}

/**
 * Extension function to map [DarkThemeConfigPreferences] to [DarkThemeConfig].
 *
 * @return The mapped [DarkThemeConfig].
 */
fun DarkThemeConfigPreferences.toDarkThemeConfig(): DarkThemeConfig {
    return when (this) {
        DarkThemeConfigPreferences.FOLLOW_SYSTEM -> DarkThemeConfig.FOLLOW_SYSTEM
        DarkThemeConfigPreferences.LIGHT -> DarkThemeConfig.LIGHT
        DarkThemeConfigPreferences.DARK -> DarkThemeConfig.DARK
    }
}

/**
 * Extension function to map [DarkThemeConfig] to [DarkThemeConfigPreferences].
 *
 * @return The mapped [DarkThemeConfigPreferences].
 */
fun DarkThemeConfig.toDarkThemeConfigPreferences(): DarkThemeConfigPreferences {
    return when (this) {
        DarkThemeConfig.FOLLOW_SYSTEM -> DarkThemeConfigPreferences.FOLLOW_SYSTEM
        DarkThemeConfig.LIGHT -> DarkThemeConfigPreferences.LIGHT
        DarkThemeConfig.DARK -> DarkThemeConfigPreferences.DARK
    }
}



