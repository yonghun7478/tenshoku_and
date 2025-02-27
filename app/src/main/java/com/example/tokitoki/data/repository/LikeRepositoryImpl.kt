package com.example.tokitoki.data.repository

import com.example.tokitoki.data.model.LikeItemData
import com.example.tokitoki.domain.model.LikeItem
import com.example.tokitoki.domain.repository.LikeRepository
import com.example.tokitoki.ui.state.LikeTab
import javax.inject.Inject

class LikeRepositoryImpl @Inject constructor() : LikeRepository {

    private val PAGE_SIZE = 20
    override suspend fun getReceivedLikes(): Result<List<LikeItem>> = Result.success(
        createDummyLikes(LikeTab.RECEIVED.title, 0)
    )


    override suspend fun getSentLikes(): Result<List<LikeItem>> =
        Result.success(createDummyLikes(LikeTab.SENT.title, 0))

    override suspend fun getMatchedLikes(): Result<List<LikeItem>> =
        Result.success(createDummyLikes(LikeTab.MATCHED.title, 0))


    override suspend fun deleteLikeItem(itemId: Int): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun deleteSelectedLikeItems(itemIds: Set<Int>): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun loadMoreLikes(tab: String, startIndex: Int): Result<List<LikeItem>> =
        Result.success(createDummyLikes(tab, startIndex))

    // 각 탭별 더미 데이터 생성 함수 (startIndex 파라미터 추가)
    private fun createDummyLikes(tab: String, startIndex: Int): List<LikeItem> {
        val baseId = when (tab) {
            LikeTab.RECEIVED.title -> 0
            LikeTab.SENT.title -> 100
            LikeTab.MATCHED.title -> 200
            else -> 0
        }
        return List(PAGE_SIZE) {
            LikeItemData(
                id = baseId + startIndex + it, // startIndex를 사용하여 ID 계산
                thumbnail = "https://via.placeholder.com/150",
                nickname = "${tab} User ${startIndex + it}",
                age = 20 + it,
                introduction = "This is a sample introduction for ${tab} user ${startIndex + it}."
            ).toDomain()
        }
    }
}