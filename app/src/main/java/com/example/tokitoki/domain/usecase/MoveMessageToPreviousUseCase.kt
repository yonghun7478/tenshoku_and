package com.example.tokitoki.domain.usecase

import com.example.tokitoki.domain.repository.MessageListRepository
import javax.inject.Inject

interface MoveMessageToPreviousUseCase {
    suspend operator fun invoke(userId: String): Result<Unit>
}

class MoveMessageToPreviousUseCaseImpl @Inject constructor(
    private val messageRepository: MessageListRepository
) : MoveMessageToPreviousUseCase {
    override suspend fun invoke(userId: String): Result<Unit> {
        return messageRepository.moveMessageToPrevious(userId)
    }
} 