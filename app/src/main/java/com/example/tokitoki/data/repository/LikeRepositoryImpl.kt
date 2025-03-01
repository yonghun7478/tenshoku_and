package com.example.tokitoki.data.repository

import com.example.tokitoki.data.model.LikeItemData
import com.example.tokitoki.domain.model.LikeItem
import com.example.tokitoki.domain.repository.LikeRepository
import com.example.tokitoki.ui.state.LikeTab
import kotlinx.coroutines.delay
import javax.inject.Inject

class LikeRepositoryImpl @Inject constructor() : LikeRepository {
    private val PAGE_SIZE = 20

    // 각 탭별 데이터를 저장할 MutableList.  초기 데이터 생성.
    private val receivedLikes = createInitialDummyLikes(LikeTab.RECEIVED.title, 0)
    private val sentLikes = createInitialDummyLikes(LikeTab.SENT.title, 0)
    private val matchedLikes = createInitialDummyLikes(LikeTab.MATCHED.title, 0)


    // 초기 더미 데이터 생성 (loadLikes()에서 사용)
    private fun createInitialDummyLikes(tab: String, startIndex: Int): MutableList<LikeItem> {
        return createDummyLikes(tab, startIndex).toMutableList()
    }

    // 더미데이터 생성 함수는 인자로 받은 tab, startIndex기반으로 아이템을 생성해서 반환
    override suspend fun getReceivedLikes(): Result<List<LikeItem>> {
        delay(500)
        return Result.success(receivedLikes)
    }

    override suspend fun getSentLikes(): Result<List<LikeItem>> {
        delay(500)
        return Result.success(sentLikes)
    }

    override suspend fun getMatchedLikes(): Result<List<LikeItem>> {
        delay(500)
        return Result.success(matchedLikes)
    }


    override suspend fun deleteLikeItem(itemId: Int): Result<Unit> {
        // 해당 ID를 가진 아이템을 모든 리스트에서 찾아서 삭제
        receivedLikes.removeAll { it.id == itemId }
        sentLikes.removeAll { it.id == itemId }
        matchedLikes.removeAll { it.id == itemId }
        return Result.success(Unit)
    }

    override suspend fun clearLikeItem(tab: String): Result<Unit> {
        when (tab) {
            LikeTab.RECEIVED.title -> {
                receivedLikes.clear()
            }

            LikeTab.SENT.title -> {
                sentLikes.clear()
            }

            LikeTab.MATCHED.title -> {
                matchedLikes.clear()
            }
        }
        return Result.success(Unit)
    }

    override suspend fun deleteSelectedLikeItems(itemIds: Set<Int>): Result<Unit> {
        // 해당 ID들을 가진 아이템을 모든 리스트에서 찾아서 삭제
        receivedLikes.removeAll { it.id in itemIds }
        sentLikes.removeAll { it.id in itemIds }
        matchedLikes.removeAll { it.id in itemIds }
        return Result.success(Unit)
    }

    override suspend fun loadMoreLikes(tab: String, startIndex: Int): Result<List<LikeItem>> {
        delay(500)
        return Result.success(createDummyLikes(tab, startIndex))
    }

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