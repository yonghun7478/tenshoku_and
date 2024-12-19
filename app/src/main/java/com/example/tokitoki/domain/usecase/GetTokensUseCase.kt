package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.Tokens
import com.example.tokitoki.domain.repository.AuthRepository
import javax.inject.Inject

interface GetTokensUseCase {
    fun execute(): Tokens
}

class GetTokensUseCaseImpl @Inject constructor(private val authRepository: AuthRepository) :
    GetTokensUseCase {
    override fun execute(): Tokens {
        return authRepository.getTokens()
    }
}