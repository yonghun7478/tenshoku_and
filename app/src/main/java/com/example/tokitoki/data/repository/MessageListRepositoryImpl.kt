package com.example.tokitoki.data.repository

import android.util.Base64
import com.example.tokitoki.data.model.MatchingUserDto
import com.example.tokitoki.data.model.PreviousChatDto
import com.example.tokitoki.data.model.toDomain
import com.example.tokitoki.domain.model.CursorResult
import com.example.tokitoki.domain.model.MatchingUser
import com.example.tokitoki.domain.model.PreviousChat
import com.example.tokitoki.domain.repository.MessageListRepository
import kotlinx.coroutines.delay
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.ZoneOffset
import javax.inject.Inject
import kotlin.random.Random
import com.example.tokitoki.data.dummy.DummyData
import com.example.tokitoki.domain.model.UserDetail

class MessageListRepositoryImpl @Inject constructor() : MessageListRepository {

    // --- 더미 데이터 생성 ---
    private val dummyMatchingUsersDatabase = mutableListOf<MatchingUserDto>().apply {
        addAll(DummyData.getUsers().mapIndexed { index, userDetail ->
            val timestamp = System.currentTimeMillis() - Random.nextLong(1000 * 60 * 60 * 24 * 7) // 최근 1주일 내 랜덤 시간
            MatchingUserDto(
                userId = userDetail.id,
                userName = userDetail.name,
                profileImageUrl = userDetail.thumbnailUrl,
                matchedTimestamp = timestamp
            )
        }.sortedByDescending { it.matchedTimestamp }) // 최신순 정렬
    }

    private val dummyPreviousChatsDatabase = mutableListOf<PreviousChatDto>().apply {
        addAll(DummyData.getUsers().mapIndexed { index, userDetail ->
            val date = LocalDate.now().minusDays(Random.nextLong(0, 90)) // 오늘부터 90일 전까지 랜덤 날짜
            val timestamp = date.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
            PreviousChatDto(
                messageId = "msg_${userDetail.id}_${timestamp}", // unique message ID
                partnerNickname = userDetail.name,
                partnerHometown = userDetail.location,
                lastMessageTimestamp = timestamp,
                partnerProfileImageUrl = userDetail.thumbnailUrl
            )
        }.sortedByDescending { it.lastMessageTimestamp }) // 최신순 정렬
    }
    // --- 더미 데이터 끝 ---


    override suspend fun getMatchingUsers(cursor: String?, limit: Int): Result<CursorResult<MatchingUser>> {
        return try {
            delay(300) // Simulate network delay

            // 커서 디코딩 (여기서는 timestamp 사용 가정)
            val cursorTimestamp = cursor?.let { decodeTimestampCursor(it) }

            // 커서를 기준으로 데이터 필터링
            val filteredData = if (cursorTimestamp != null) {
                dummyMatchingUsersDatabase.filter { it.matchedTimestamp < cursorTimestamp }
            } else {
                dummyMatchingUsersDatabase // 첫 페이지
            }

            val pageData = filteredData.take(limit)
            val nextCursor = if (pageData.size == limit && filteredData.size > limit) {
                // 마지막 아이템의 timestamp로 다음 커서 생성
                encodeTimestampCursor(pageData.last().matchedTimestamp)
            } else {
                null // 다음 페이지 없음
            }

            val domainData = pageData.map { it.toDomain() }
            Result.success(CursorResult(data = domainData, nextCursor = nextCursor))

        } catch (e: Exception) {
            // 실제 구현에서는 에러 종류에 따라 구체적인 처리 필요
            Result.failure(e)
        }
    }

    override suspend fun getPreviousChats(cursor: String?, limit: Int): Result<CursorResult<PreviousChat>> {
        return try {
            delay(500) // Simulate network delay

            // 커서 디코딩 ("messageId:timestamp")
            val (cursorMessageId, cursorTimestamp) = cursor?.let { decodePreviousChatCursor(it) } ?: (null to null)

            // 커서를 기준으로 데이터 필터링
            val filteredData = if (cursorTimestamp != null && cursorMessageId != null) {
                // 커서의 타임스탬프보다 작거나, 같다면 메시지 ID가 작은 것을 가져옴 (중복 방지 및 정렬 유지)
                 dummyPreviousChatsDatabase.filter {
                    it.lastMessageTimestamp < cursorTimestamp ||
                    (it.lastMessageTimestamp == cursorTimestamp && it.messageId < cursorMessageId) // ID 비교는 문자열 비교로 가정
                }
            } else {
                dummyPreviousChatsDatabase // 첫 페이지
            }

            val pageData = filteredData.take(limit)
            val nextCursor = if (pageData.size == limit && filteredData.size > limit) {
                // 마지막 아이템의 정보로 다음 커서 생성
                val lastItem = pageData.last()
                encodePreviousChatCursor(lastItem.messageId, lastItem.lastMessageTimestamp)
            } else {
                null // 다음 페이지 없음
            }

            val domainData = pageData.map { it.toDomain() }
            Result.success(CursorResult(data = domainData, nextCursor = nextCursor))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun moveMessageToPrevious(userId: String): Result<Unit> {
        return try {
            delay(500) // Simulate network delay

            // 매칭된 유저 목록에서 해당 유저 찾기
            val matchingUser = dummyMatchingUsersDatabase.find { it.userId == userId }
                ?: return Result.failure(IllegalArgumentException("User not found in matching list"))

            // 이전 대화 목록에 추가
            val newPreviousChat = PreviousChatDto(
                messageId = "msg_${System.currentTimeMillis()}",
                partnerNickname = matchingUser.userName,
                partnerHometown = "Unknown", // 실제 구현에서는 유저의 고향 정보를 가져와야 함
                lastMessageTimestamp = System.currentTimeMillis(),
                partnerProfileImageUrl = matchingUser.profileImageUrl
            )

            // 이전 대화 목록에 추가하고 시간순으로 정렬
            dummyPreviousChatsDatabase.add(newPreviousChat)
            dummyPreviousChatsDatabase.sortByDescending { it.lastMessageTimestamp }

            // 매칭된 유저 목록에서 제거
            dummyMatchingUsersDatabase.removeIf { it.userId == userId }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- 커서 인코딩/디코딩 헬퍼 함수 ---

    private fun encodeTimestampCursor(timestamp: Long): String {
        return Base64.encodeToString(timestamp.toString().toByteArray(StandardCharsets.UTF_8), Base64.NO_WRAP)
    }

    private fun decodeTimestampCursor(cursor: String): Long? {
        return try {
            String(Base64.decode(cursor, Base64.NO_WRAP), StandardCharsets.UTF_8).toLongOrNull()
        } catch (e: IllegalArgumentException) {
            null // 잘못된 형식의 커서
        }
    }

    private fun encodePreviousChatCursor(messageId: String, timestamp: Long): String {
        val data = "$messageId:$timestamp"
        return Base64.encodeToString(data.toByteArray(StandardCharsets.UTF_8), Base64.NO_WRAP)
    }

    private fun decodePreviousChatCursor(cursor: String): Pair<String, Long>? {
        return try {
            val decoded = String(Base64.decode(cursor, Base64.NO_WRAP), StandardCharsets.UTF_8)
            val parts = decoded.split(":", limit = 2)
            if (parts.size == 2) {
                val messageId = parts[0]
                val timestamp = parts[1].toLongOrNull()
                if (timestamp != null) {
                    messageId to timestamp
                } else {
                    null
                }
            } else {
                null
            }
        } catch (e: Exception) { // IllegalArgumentException 등
            null
        }
    }
}