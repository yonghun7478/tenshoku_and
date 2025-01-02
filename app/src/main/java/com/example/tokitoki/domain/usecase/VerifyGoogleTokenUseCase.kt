package com.example.tokitoki.domain.usecase

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.model.VerifyGoogleToken
import com.example.tokitoki.domain.repository.AuthRepository
import javax.inject.Inject

interface VerifyGoogleTokenUseCase {
    suspend operator fun invoke(id: String, idToken: String): ResultWrapper<VerifyGoogleToken>
}

class VerifyGoogleTokenUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : VerifyGoogleTokenUseCase {
    override suspend operator fun invoke(id: String, idToken: String): ResultWrapper<VerifyGoogleToken> {
        return authRepository.verifyGoogleToken(id, idToken)
    }
}