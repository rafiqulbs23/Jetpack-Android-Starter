package com.aristopharma.v2.feature.profile.domain.usecase

import com.aristopharma.v2.feature.profile.domain.model.Profile
import com.aristopharma.v2.feature.profile.domain.repository.ProfileRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetProfileUseCase @Inject constructor(
    private val repository: ProfileRepository,
) {
    operator fun invoke(): Flow<Profile> = repository.getProfile()
}

