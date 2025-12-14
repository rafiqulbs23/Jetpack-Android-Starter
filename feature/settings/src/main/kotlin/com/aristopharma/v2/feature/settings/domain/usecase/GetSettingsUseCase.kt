package com.aristopharma.v2.feature.settings.domain.usecase

import com.aristopharma.v2.feature.settings.domain.model.Settings
import com.aristopharma.v2.feature.settings.domain.repository.SettingsRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository,
) {
    operator fun invoke(): Flow<Settings> = repository.getSettings()
}


