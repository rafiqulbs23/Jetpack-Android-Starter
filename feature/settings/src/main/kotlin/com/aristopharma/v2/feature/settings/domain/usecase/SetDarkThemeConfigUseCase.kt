package com.aristopharma.v2.feature.settings.domain.usecase

import com.aristopharma.v2.feature.settings.domain.model.DarkThemeConfig
import com.aristopharma.v2.feature.settings.domain.repository.SettingsRepository
import javax.inject.Inject

class SetDarkThemeConfigUseCase @Inject constructor(
    private val repository: SettingsRepository,
) {
    suspend operator fun invoke(config: DarkThemeConfig) =
        repository.setDarkThemeConfig(config)
}

