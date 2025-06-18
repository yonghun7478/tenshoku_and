package com.example.tokitoki.data.repository

import android.util.Log
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
import com.example.tokitoki.data.dummy.DummyData
import com.example.tokitoki.domain.model.UserDetail

class MessageRepositoryImpl @Inject constructor() : MessageRepository {
    private val TAG = "MessageRepoImpl"
    // 더미 데이터를 저장할 리스트 (유저 ID별로 관리)
    private val dummyMessages = mutableMapOf<String, CopyOnWriteArrayList<Message>>()
    private val messageStatuses = mutableMapOf<String, MessageStatus>()
    private var messageIdCounter = 0L // 메시지 ID 카운터 추가

    init {
        // '이전 대화가 있는' 더미 유저에 대해서만 초기 메시지 데이터 생성
        DummyData.usersWithPreviousChat.forEach { userDetail ->
            val messagesForUser = CopyOnWriteArrayList<Message>()
            repeat(20) { index -> // 각 유저마다 20개의 더미 메시지 생성
                val isFromMe = index % 2 == 0
                messagesForUser.add(
                    Message(
                        id = "msg_${userDetail.id}_" + (messageIdCounter++).toString(), // 유저 ID와 증가하는 숫자 ID 사용
                        senderId = if (isFromMe) "current_user" else userDetail.id,
                        receiverId = if (isFromMe) userDetail.id else "current_user",
                        content = "더미 메시지 ${index + 1} from ${if (isFromMe) "나" else userDetail.name}",
                        timestamp = System.currentTimeMillis() - (index * 1000L) - (userDetail.id.toIntOrNull() ?: 0) * 100000L, // 유저별 시간차
                        isRead = true,
                        isFromMe = isFromMe
                    )
                )
            }
            messagesForUser.sortByDescending { it.timestamp } // 최신순 정렬
            dummyMessages[userDetail.id] = messagesForUser
        }
    }

    override suspend fun getMessageHistory(userId: String, cursor: String?, limit: Int): Result<CursorResult<Message>> {
        return try {
            Log.d(TAG, "getMessageHistory - userId: $userId, cursor: $cursor")
            delay(1000) // 네트워크 지연 시뮬레이션

            val messagesForUser = dummyMessages[userId] ?: return Result.success(CursorResult(emptyList(), null))

            val cursorTimestamp = cursor?.toLongOrNull() ?: Long.MAX_VALUE

            // 커서 타임스탬프보다 엄격히 이전인 메시지부터 찾기
            val startIndex = messagesForUser.indexOfFirst { it.timestamp < cursorTimestamp }.let {
                 if (it == -1) messagesForUser.size else it
            }

            val endIndex = minOf(startIndex + limit, messagesForUser.size)

            Log.d(TAG, "getMessageHistory - startIndex: $startIndex, endIndex: $endIndex")

            val messagesToFetch = if (startIndex < endIndex) {
                 messagesForUser.subList(startIndex, endIndex).toList()
            } else {
                 emptyList()
            }

            // 다음 커서 계산
            val nextCursor = if (messagesToFetch.isNotEmpty() && endIndex < messagesForUser.size) {
                messagesToFetch.last().timestamp.toString()
            } else {
                null
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
                id = "msg_${userId}_" + (messageIdCounter++).toString(), // 유저 ID와 증가하는 숫자 ID 사용
                senderId = "current_user",
                receiverId = userId,
                content = message,
                timestamp = System.currentTimeMillis(),
                isRead = false,
                isFromMe = true
            )
            
            dummyMessages[userId]?.add(0, newMessage) // 해당 유저의 메시지 리스트 앞에 새 메시지 추가

            // 생성된 newMessage 객체를 Result.success()에 담아 반환
            Result.success(newMessage)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun receiveMessages(userId: String): Flow<Message> = flow {
        // 실시간 메시지 수신 시뮬레이션
        while (true) {
            delay(10000) // 10초마다 새 메시지 생성
            
            val newMessage = Message(
                id = "msg_${userId}_" + (messageIdCounter++).toString(), // 유저 ID와 증가하는 숫자 ID 사용
                senderId = userId,
                receiverId = "current_user",
                content = "새로운 메시지가 도착했습니다! (${System.currentTimeMillis()})",
                timestamp = System.currentTimeMillis(),
                isRead = false,
                isFromMe = false
            )
            
            dummyMessages[userId]?.add(0, newMessage)
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