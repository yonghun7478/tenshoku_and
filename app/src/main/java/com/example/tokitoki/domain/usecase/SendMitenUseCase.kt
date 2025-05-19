package com.example.tokitoki.domain.usecase

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.repository.UserRepository
import javax.inject.Inject

interface SendMitenUseCase {
    suspend operator fun invoke(userId: String): ResultWrapper<Unit>
}

class SendMitenUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : SendMitenUseCase {
    override suspend operator fun invoke(userId: String): ResultWrapper<Unit> {
        return userRepository.sendMiten(userId)
    }
} 