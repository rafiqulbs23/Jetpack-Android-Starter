package com.aristopharma.v2.feature.profile.domain.usecase

import com.aristopharma.v2.feature.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class SignOutProfileUseCase @Inject constructor(
    private val repository: ProfileRepository,
) {
    suspend operator fun invoke() = repository.signOut()
}

