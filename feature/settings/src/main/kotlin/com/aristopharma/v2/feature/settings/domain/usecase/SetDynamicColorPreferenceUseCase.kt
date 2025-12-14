package com.aristopharma.v2.feature.settings.domain.usecase

import com.aristopharma.v2.feature.settings.domain.repository.SettingsRepository
import javax.inject.Inject

class SetDynamicColorPreferenceUseCase @Inject constructor(
    private val repository: SettingsRepository,
) {
    suspend operator fun invoke(useDynamicColor: Boolean) =
        repository.setDynamicColorPreference(useDynamicColor)
}


