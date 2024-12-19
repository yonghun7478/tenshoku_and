package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.repository.AuthRepository
import javax.inject.Inject

interface SaveTokensUseCase {
    fun execute(token: String, refreshToken: String)
}

class SaveTokensUseCaseImpl @Inject constructor(private val authRepository: AuthRepository) :
    SaveTokensUseCase {
    override fun execute(token: String, refreshToken: String) {
        authRepository.saveTokens(token, refreshToken)
    }
}
