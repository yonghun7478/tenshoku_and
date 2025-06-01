package com.example.tokitoki.data.repository

import com.example.tokitoki.domain.model.Message
import com.example.tokitoki.domain.model.MessageStatus
import com.example.tokitoki.domain.model.CursorResult
import com.example.tokitoki.domain.repository.MessageRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject
import java.util.concurrent.CopyOnWriteArrayList

class MessageRepositoryImpl @Inject constructor() : MessageRepository {
    // 더미 데이터를 저장할 리스트
    private val dummyMessages = CopyOnWriteArrayList<Message>()
    private val messageStatuses = mutableMapOf<String, MessageStatus>()

    init {
        // 초기 더미 데이터 생성
        repeat(20) { index ->
            val isFromMe = index % 2 == 0
            dummyMessages.add(
                Message(
                    id = UUID.randomUUID().toString(),
                    senderId = if (isFromMe) "current_user" else "other_user",
                    receiverId = if (isFromMe) "other_user" else "current_user",
                    content = "더미 메시지 ${index + 1}",
                    timestamp = System.currentTimeMillis() - (index * 1000L),
                    isRead = true,
                    isFromMe = isFromMe
                )
            )
        }
    }

    override suspend fun getMessageHistory(userId: String, cursor: String?, limit: Int): Result<CursorResult<Message>> {
        // 실제 구현에서는 서버에서 데이터를 가져오지만, 여기서는 더미 데이터를 반환
        return try {
            delay(1000) // 네트워크 지연 시뮬레이션
            
            val startIndex = cursor?.toIntOrNull() ?: 0
            val endIndex = minOf(startIndex + limit, dummyMessages.size)
            val messages = dummyMessages.subList(startIndex, endIndex).toList()
            
            val nextCursor = if (endIndex < dummyMessages.size) endIndex.toString() else null
            
            Result.success(CursorResult(messages, nextCursor))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendMessage(userId: String, message: String): Result<Message> {
        return try {
            delay(500) // 네트워크 지연 시뮬레이션
            
            val newMessage = Message(
                id = UUID.randomUUID().toString(),
                senderId = "current_user",
                receiverId = userId,
                content = message,
                timestamp = System.currentTimeMillis(),
                isRead = false,
                isFromMe = true
            )
            
            dummyMessages.add(0, newMessage) // 새 메시지를 리스트 앞에 추가

            // 생성된 newMessage 객체를 Result.success()에 담아 반환
            Result.success(newMessage)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun receiveMessages(userId: String): Flow<Message> = flow {
        // 실시간 메시지 수신 시뮬레이션
        while (true) {
            delay(10000) // 5초마다 새 메시지 생성
            
            val newMessage = Message(
                id = UUID.randomUUID().toString(),
                senderId = userId,
                receiverId = "current_user",
                content = "새로운 메시지가 도착했습니다! (${System.currentTimeMillis()})",
                timestamp = System.currentTimeMillis(),
                isRead = false,
                isFromMe = false
            )
            
            dummyMessages.add(0, newMessage)
            emit(newMessage)
        }
    }

    override suspend fun updateMessageStatus(userId: String, hasMessages: Boolean): Result<Unit> {
        return try {
            delay(300) // 네트워크 지연 시뮬레이션
            messageStatuses[userId] = if (hasMessages) MessageStatus.Online else MessageStatus.Offline
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMessageStatus(userId: String): Result<MessageStatus> {
        return try {
            delay(300) // 네트워크 지연 시뮬레이션
            Result.success(messageStatuses[userId] ?: MessageStatus.Offline)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 