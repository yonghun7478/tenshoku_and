package com.example.tokitoki.domain.usecase.message

import com.example.tokitoki.domain.repository.MessageRepository
import javax.inject.Inject

interface MoveMessageToPreviousUseCase {
    suspend operator fun invoke(userId: String): Result<Unit>
}

class MoveMessageToPreviousUseCaseImpl @Inject constructor(
    private val messageRepository: MessageRepository
) : MoveMessageToPreviousUseCase {
    override suspend fun invoke(userId: String): Result<Unit> {
        return messageRepository.moveMessageToPrevious(userId)
    }
} 