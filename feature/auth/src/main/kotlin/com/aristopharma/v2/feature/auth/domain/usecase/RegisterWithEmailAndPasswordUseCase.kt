package com.aristopharma.v2.feature.auth.domain.usecase

import android.app.Activity
import com.aristopharma.v2.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterWithEmailAndPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String,
        activity: Activity,
    ) = authRepository.registerWithEmailAndPassword(name, email, password, activity)
}

