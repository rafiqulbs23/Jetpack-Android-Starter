package com.aristopharma.v2.feature.auth.domain.usecase

import com.aristopharma.v2.feature.auth.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithEmailAndPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(email: String, password: String) =
        authRepository.signInWithEmailAndPassword(email, password)
}

