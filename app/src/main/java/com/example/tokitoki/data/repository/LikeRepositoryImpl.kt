package com.example.tokitoki.data.repository

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.common.ResultWrapper.ErrorType
import com.example.tokitoki.data.model.LikeItemData
import com.example.tokitoki.domain.model.LikeItem
import com.example.tokitoki.domain.model.LikeResult
import com.example.tokitoki.domain.repository.LikeRepository
import com.example.tokitoki.ui.state.LikeTab
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.random.Random

class LikeRepositoryImpl @Inject constructor() : LikeRepository {

    private val PAGE_SIZE = 100

    // 각 탭별 데이터를 저장할 맵 (MutableMap)
    private val allLikes = mutableMapOf<String, MutableList<LikeItem>>() // 키: 탭, 값: LikeItem 리스트

    // 초기 데이터 생성 (init 블록)
    init {
        allLikes[LikeTab.RECEIVED.title] = createDummyLikes(LikeTab.RECEIVED.title, 0).toMutableList()
        allLikes[LikeTab.SENT.title] = createDummyLikes(LikeTab.SENT.title, 0).toMutableList()
        allLikes[LikeTab.MATCHED.title] = createDummyLikes(LikeTab.MATCHED.title, 0).toMutableList()
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
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            ResultWrapper.Error(ErrorType.ExceptionError(e.message ?: "좋아요 추가 중 오류가 발생했습니다."))
        }
    }

    override suspend fun isUserLiked(userId: String): ResultWrapper<Boolean> {
        delay(100) // Simulate network delay
        // 더미 구현: userId가 "1"이거나 짝수 ID일 경우 true 반환
        val isLiked = userId == "1" || (userId.toIntOrNull()?.let { it % 2 == 0 } ?: false)
        return ResultWrapper.Success(isLiked)
    }

    // 각 탭별 더미 데이터 생성 함수
    private fun createDummyLikes(tab: String, startIndex: Int): List<LikeItem> {
        val baseId = when (tab) {
            LikeTab.RECEIVED.title -> 0
            LikeTab.SENT.title -> 100
            LikeTab.MATCHED.title -> 200
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