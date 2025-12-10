package com.aristopharma.v2.feature.settings.domain.usecase

import com.aristopharma.v2.feature.settings.domain.repository.SettingsRepository
import javax.inject.Inject

class SignOutSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository,
) {
    suspend operator fun invoke() = repository.signOut()
}

