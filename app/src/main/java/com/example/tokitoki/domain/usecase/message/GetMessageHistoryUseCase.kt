package com.example.tokitoki.domain.usecase.message

import com.example.tokitoki.domain.model.Message
import com.example.tokitoki.domain.model.CursorResult
import com.example.tokitoki.domain.repository.MessageRepository
import javax.inject.Inject

interface GetMessageHistoryUseCase {
    suspend operator fun invoke(userId: String, cursor: String?, limit: Int = 10): Result<CursorResult<Message>>
}

class GetMessageHistoryUseCaseImpl @Inject constructor(
    private val messageRepository: MessageRepository
) : GetMessageHistoryUseCase {
    override suspend fun invoke(userId: String, cursor: String?, limit: Int): Result<CursorResult<Message>> {
        return messageRepository.getMessageHistory(userId, cursor, limit)
    }
} 