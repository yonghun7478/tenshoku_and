package com.example.tokitoki.data.repository

import com.example.tokitoki.data.model.LikeItemData
import com.example.tokitoki.domain.model.LikeItem
import com.example.tokitoki.domain.model.LikeResult
import com.example.tokitoki.domain.repository.LikeRepository
import com.example.tokitoki.ui.state.LikeTab
import kotlinx.coroutines.delay
import javax.inject.Inject

class LikeRepositoryImpl @Inject constructor() : LikeRepository {

    private val PAGE_SIZE = 20

    override suspend fun getLikes(tab: String, cursor: Long?, limit: Int): Result<LikeResult> {
        delay(500)

        // 더미 데이터 생성 (cursor 사용 안 함, startIndex = 0)
        val allData = createDummyLikes(tab, 0) // 처음부터 PAGE_SIZE * 3 만큼 생성
        // 좋아요 받은 시간(receivedTime)으로 내림차순 정렬
        val sortedData = allData.sortedByDescending { it.receivedTime }

        val startIndex = if (cursor == null) 0 else sortedData.indexOfFirst { it.receivedTime <= cursor }

        if (startIndex == -1 || startIndex >= sortedData.size) {
            return Result.success(LikeResult(emptyList(), null)) // 더 이상 데이터 없음 or cursor에 해당하는 데이터 없음
        }

        val endIndex = (startIndex + limit).coerceAtMost(sortedData.size) // endIndex 계산
        val newData = sortedData.subList(startIndex, endIndex) // subList를 사용하여 범위 내 데이터 가져오기
        val nextCursor = newData.lastOrNull()?.receivedTime // 다음 페이지 커서는 마지막 아이템의 receivedTime

        return Result.success(LikeResult(newData, nextCursor))
    }

    // 각 탭별 더미 데이터 생성 함수, 처음 3페이지 분량(startIndex = 0, 1, 2) 데이터를 생성
    // 처음 3페이지 분량(startIndex = 0,20,40) 데이터를 생성
    private fun createDummyLikes(tab: String, startIndex: Int): List<LikeItem> {
        val baseId = when (tab) {
            LikeTab.RECEIVED.title -> 0
            LikeTab.SENT.title -> 100
            LikeTab.MATCHED.title -> 200
            else -> 0
        }
        val now = System.currentTimeMillis()
        //startIndex = 0, pageSize = 20으로 고정
        return List(PAGE_SIZE) { index ->
            val id = baseId + startIndex + index
            LikeItemData(
                id = id,
                thumbnail = "https://via.placeholder.com/150",
                nickname = "${tab} User ${startIndex + index}",
                age = 20 + (id % 10),
                introduction = "This is a sample introduction for $tab user $id.",
                receivedTime = now - (startIndex + index) * 60000L // 1분 간격
            ).toDomain()
        }
    }
}