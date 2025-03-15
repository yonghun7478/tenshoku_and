package com.example.tokitoki.data.repository

import com.example.tokitoki.data.model.LikeItemData
import com.example.tokitoki.domain.model.LikeItem
import com.example.tokitoki.domain.model.LikeResult
import com.example.tokitoki.domain.repository.LikeRepository
import com.example.tokitoki.ui.state.LikeTab
import kotlinx.coroutines.delay
import javax.inject.Inject

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

        // 1. 해당 탭의 데이터 가져오기
        val data = allLikes[tab] ?: mutableListOf() // 해당 탭 데이터 없으면 빈 리스트

        // 2. cursor를 이용하여 startIndex 계산
        val startIndex = if (cursor == null) 0 else {
            data.indexOfFirst { it.receivedTime <= cursor }
        }

        if (startIndex == -1) { // cursor에 해당하는 아이템이 없거나, 더이상 로드할 데이터가 없을때
            return Result.success(LikeResult(emptyList(), null))
        }

        // 3. endIndex 계산 및 데이터 자르기
        val endIndex = (startIndex + limit).coerceAtMost(data.size)

        //startIndex가 data.size보다 크거나 같으면 더이상 data가 없다.
        if (startIndex >= data.size) {
            return Result.success(LikeResult(emptyList(), null)) // 더 이상 데이터 없음 or cursor에 해당하는 데이터 없음
        }

        val newData = data.subList(startIndex, endIndex)

        // 4. nextCursor 계산
        val nextCursor = newData.lastOrNull()?.receivedTime

        return Result.success(LikeResult(newData, nextCursor))
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
            val id = baseId + startIndex + index  // startIndex 반영
            LikeItemData(
                id = id,
                thumbnail = "https://via.placeholder.com/150",
                nickname = "${tab} User ${startIndex + index}", // startIndex 반영
                age = 20 + (id % 10),
                introduction = "This is a sample introduction for $tab user $id.",
                receivedTime = now - (startIndex + index) * 60000L // startIndex 반영
            ).toDomain()
        }
    }
}