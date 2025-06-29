package com.example.tokitoki.domain.usecase.message

import com.example.tokitoki.domain.model.Message
import com.example.tokitoki.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ReceiveMessageUseCase {
    suspend operator fun invoke(userId: String): Flow<Message>
}

class ReceiveMessageUseCaseImpl @Inject constructor(
    private val messageRepository: MessageRepository
) : ReceiveMessageUseCase {
    override suspend fun invoke(userId: String): Flow<Message> {
        return messageRepository.receiveMessages(userId)
    }
} 