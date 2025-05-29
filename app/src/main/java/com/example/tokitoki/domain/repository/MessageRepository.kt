package com.example.tokitoki.domain.repository

import com.example.tokitoki.domain.model.Message
import com.example.tokitoki.domain.model.MessageStatus
import com.example.tokitoki.domain.model.CursorResult
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun getMessageHistory(userId: String, cursor: String?, limit: Int): Result<CursorResult<Message>>
    suspend fun sendMessage(userId: String, message: String): Result<Unit>
    suspend fun receiveMessages(userId: String): Flow<Message>
    suspend fun updateMessageStatus(userId: String, hasMessages: Boolean): Result<Unit>
    suspend fun getMessageStatus(userId: String): Result<MessageStatus>
} 