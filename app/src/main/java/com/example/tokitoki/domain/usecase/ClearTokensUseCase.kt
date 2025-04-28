package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.repository.AuthRepository
import javax.inject.Inject

interface ClearTokensUseCase {
    suspend operator fun invoke()
}

class ClearTokensUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : ClearTokensUseCase {
    override suspend fun invoke() {
        authRepository.clearTokens()
    }
} 