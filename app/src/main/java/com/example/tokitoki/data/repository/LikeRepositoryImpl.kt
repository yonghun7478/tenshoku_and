package com.example.tokitoki.data.repository

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.common.ResultWrapper.ErrorType
import com.example.tokitoki.data.model.LikeItemData
import com.example.tokitoki.domain.model.LikeItem
import com.example.tokitoki.domain.model.LikeResult
import com.example.tokitoki.domain.repository.LikeRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.random.Random

class LikeRepositoryImpl @Inject constructor() : LikeRepository {

    private val PAGE_SIZE = 100

    // 각 탭별 데이터를 저장할 맵 (MutableMap)
    private val allLikes = mutableMapOf<String, MutableList<LikeItem>>() // 키: 탭, 값: LikeItem 리스트

    // 좋아요 상태를 저장할 Set
    private val likedUserIds: MutableSet<String> = mutableSetOf()

    // 초기 데이터 생성 (init 블록)
    init {
        allLikes[LikeRepository.RECEIVED] = createDummyLikes(LikeRepository.RECEIVED, 0).toMutableList()
        allLikes[LikeRepository.SENT] = createDummyLikes(LikeRepository.SENT, 0).toMutableList()
    }

    override suspend fun getLikes(tab: String, cursor: Long?, limit: Int): Result<LikeResult> {
        delay(500)

        val data = allLikes[tab] ?: mutableListOf()
        val sortedData = data.sortedByDescending { it.receivedTime } // 정렬

        val startIndex = if (cursor == null) 0 else {
            sortedData.indexOfFirst { it.receivedTime < cursor } // cursor보다 작은 첫번째 index
        }

        if (startIndex == -1 || startIndex >= sortedData.size) {
            return Result.success(LikeResult(emptyList(), null))
        }

        val endIndex = (startIndex + limit).coerceAtMost(sortedData.size)
        val newData = sortedData.subList(startIndex, endIndex)

        val nextCursor = newData.lastOrNull()?.receivedTime

        return Result.success(LikeResult(newData, nextCursor))
    }

    override suspend fun likeUser(userId: String): ResultWrapper<Unit> {
        return try {
            // TODO: 실제 API 호출 구현
            delay(500) // API 호출 시뮬레이션
            likedUserIds.add(userId) // 좋아요 성공 시 userId를 세트에 추가
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            ResultWrapper.Error(ErrorType.ExceptionError(e.message ?: "좋아요 추가 중 오류가 발생했습니다."))
        }
    }

    override suspend fun isUserLiked(userId: String): ResultWrapper<Boolean> {
        delay(100) // Simulate network delay
        // 더미 구현: likedUserIds 세트에 userId가 포함되어 있는지 반환
        return ResultWrapper.Success(likedUserIds.contains(userId))
    }

    // 각 탭별 더미 데이터 생성 함수
    private fun createDummyLikes(tab: String, startIndex: Int): List<LikeItem> {
        val baseId = when (tab) {
            LikeRepository.RECEIVED -> 0
            LikeRepository.SENT -> 100
            else -> 0
        }
        val now = System.currentTimeMillis()

        return List(PAGE_SIZE) { index ->
            val id = index  // startIndex 반영
            LikeItemData(
                id = id.toString(),
                thumbnail = "https://via.placeholder.com/150",
                nickname = "${tab} User ${startIndex + index}", // startIndex 반영
                age = 20 + (id % 10),
                introduction = "This is a sample introduction for $tab user $id.",
                receivedTime = now - (startIndex + index) * 60000L, // startIndex 반영
                location = listOf("서울", "부산", "대구", "인천", "광주").random(),
                occupation = if (Random.nextBoolean()) listOf("회사원", "학생", "프리랜서", "공무원").random() else null,
                likedAt = now - (startIndex + index) * 60000L
            ).toDomain()
        }
    }
}