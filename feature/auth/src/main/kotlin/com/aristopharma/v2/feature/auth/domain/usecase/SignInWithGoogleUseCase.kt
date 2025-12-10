package com.aristopharma.v2.feature.auth.domain.usecase

import android.app.Activity
import com.aristopharma.v2.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(activity: Activity) =
        authRepository.signInWithGoogle(activity)
}

