package com.example.tokitoki.domain.usecase.message

import com.example.tokitoki.domain.model.Message
import com.example.tokitoki.domain.repository.MessageRepository
import javax.inject.Inject

interface SendMessageUseCase {
    suspend operator fun invoke(userId: String, message: String): Result<Message>
}

class SendMessageUseCaseImpl @Inject constructor(
    private val messageRepository: MessageRepository
) : SendMessageUseCase {
    override suspend fun invoke(userId: String, message: String): Result<Message> {
        return messageRepository.sendMessage(userId, message)
    }
} 