package com.example.tokitoki.domain.usecase.message

import com.example.tokitoki.domain.model.MessageStatus
import com.example.tokitoki.domain.repository.MessageRepository
import javax.inject.Inject

interface UpdateMessageStatusUseCase {
    suspend operator fun invoke(userId: String, hasMessages: Boolean): Result<Unit>
    suspend fun getMessageStatus(userId: String): Result<MessageStatus>
}

class UpdateMessageStatusUseCaseImpl @Inject constructor(
    private val messageRepository: MessageRepository
) : UpdateMessageStatusUseCase {
    override suspend fun invoke(userId: String, hasMessages: Boolean): Result<Unit> {
        return messageRepository.updateMessageStatus(userId, hasMessages)
    }

    override suspend fun getMessageStatus(userId: String): Result<MessageStatus> {
        return messageRepository.getMessageStatus(userId)
    }
} 