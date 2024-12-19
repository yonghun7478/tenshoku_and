package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.model.Tokens
import com.example.tokitoki.domain.repository.AuthRepository
import javax.inject.Inject

interface SendVerificationCodeUseCase {
    suspend fun execute(code: String): Tokens
}

class SendVerificationCodeUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : SendVerificationCodeUseCase {
    override suspend fun execute(code: String): Tokens {
        return authRepository.sendVerificationCode(code)
    }
}