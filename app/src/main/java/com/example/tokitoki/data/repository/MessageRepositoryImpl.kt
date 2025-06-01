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
    private var messageIdCounter = 0L // 메시지 ID 카운터 추가

    init {
        // 초기 더미 데이터 생성
        repeat(40) { index ->
            val isFromMe = index % 2 == 0
            dummyMessages.add(
                Message(
                    id = (messageIdCounter++).toString(), // 증가하는 숫자 ID 사용
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
        return try {
            delay(1000) // 네트워크 지연 시뮬레이션

            val cursorTimestamp = cursor?.toLongOrNull() ?: Long.MAX_VALUE

            // 커서 타임스탬프보다 엄격히 이전인 메시지부터 찾기
            val startIndex = dummyMessages.indexOfFirst { it.timestamp < cursorTimestamp }.let {
                 if (it == -1) dummyMessages.size else it
            }

            val endIndex = minOf(startIndex + limit, dummyMessages.size)

            val messagesToFetch = if (startIndex < endIndex) {
                 dummyMessages.subList(startIndex, endIndex).toList()
            } else {
                 emptyList()
            }

            // 다음 커서 계산
            val nextCursor = if (messagesToFetch.isNotEmpty() && endIndex < dummyMessages.size) {
                messagesToFetch.last().timestamp.toString()
            } else {
                null // 불필요한 역슬래시 및 개행 제거
            }

            Result.success(CursorResult(messagesToFetch, nextCursor))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendMessage(userId: String, message: String): Result<Message> {
        return try {
            delay(500) // 네트워크 지연 시뮬레이션
            
            val newMessage = Message(
                id = (messageIdCounter++).toString(), // 증가하는 숫자 ID 사용
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
                id = (messageIdCounter++).toString(), // 증가하는 숫자 ID 사용
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