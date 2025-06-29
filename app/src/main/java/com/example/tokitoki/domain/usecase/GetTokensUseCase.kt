package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.Tokens
import com.example.tokitoki.domain.repository.AuthRepository
import javax.inject.Inject

interface GetTokensUseCase {
    suspend operator fun invoke(): Tokens
}

class GetTokensUseCaseImpl @Inject constructor(private val authRepository: AuthRepository) :
    GetTokensUseCase {
    override suspend fun invoke(): Tokens {
        return authRepository.getTokens()
    }
}