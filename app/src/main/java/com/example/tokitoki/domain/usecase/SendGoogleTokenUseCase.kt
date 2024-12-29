package com.example.tokitoki.domain.usecase

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.model.VerifyGoogleToken
import com.example.tokitoki.domain.repository.AuthRepository
import javax.inject.Inject

interface SendGoogleTokenUseCase {
    suspend operator fun invoke(id: String, idToken: String): ResultWrapper<VerifyGoogleToken>
}

class SendGoogleTokenUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : SendGoogleTokenUseCase {
    override suspend operator fun invoke(id: String, idToken: String): ResultWrapper<VerifyGoogleToken> {
        return authRepository.sendGoogleToken(id, idToken)
    }
}