package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.repository.AuthRepository
import javax.inject.Inject

interface SaveTokensUseCase {
    suspend operator fun invoke(token: String, refreshToken: String)
}

class SaveTokensUseCaseImpl @Inject constructor(private val authRepository: AuthRepository) :
    SaveTokensUseCase {
    override suspend fun invoke(token: String, refreshToken: String) {
        authRepository.saveTokens(token, refreshToken)
    }
}
