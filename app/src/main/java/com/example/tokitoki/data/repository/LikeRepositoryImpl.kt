package com.example.tokitoki.data.repository

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.common.ResultWrapper.ErrorType
import com.example.tokitoki.data.dummy.DummyData
import com.example.tokitoki.data.model.LikeItemData
import com.example.tokitoki.domain.model.LikeItem
import com.example.tokitoki.domain.model.LikeResult
import com.example.tokitoki.domain.model.UserDetail
import com.example.tokitoki.domain.repository.LikeRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.random.Random

class LikeRepositoryImpl @Inject constructor() : LikeRepository {

    private val PAGE_SIZE = 100
    private val CURRENT_USER_ID = "0" // 현재 사용자의 ID를 "0"으로 고정

    // 각 탭별 데이터를 저장할 맵 (MutableMap)
    private val allLikes = mutableMapOf<String, MutableList<LikeItem>>() // 키: 탭, 값: LikeItem 리스트

    // 좋아요 상태를 저장할 Set (보낸 사람 ID, 받은 사람 ID)
    private val likedPairs: MutableSet<Pair<String, String>> = mutableSetOf()

    private val _users = DummyData.getUsers()

    // 초기 데이터 생성 (init 블록)
    init {
        // 더미 사용자 데이터 가져오기
        val users = DummyData.getUsers()
        
        // 더미 좋아요 관계 생성
        val dummyReceivedLikes = mutableListOf<LikeItem>()
        val dummySentLikes = mutableListOf<LikeItem>()

        // 예시: 100명의 사용자에게 임의로 좋아요를 받거나 보내는 시나리오
        val initialReceivedTime = System.currentTimeMillis()
        users.forEachIndexed { index, user ->
            if (user.id != CURRENT_USER_ID) { // 현재 사용자가 아닐 경우에만 좋아요 관계 생성
                // 현재 사용자가 다른 사용자에게 좋아요를 보냄
                if (dummySentLikes.size < 5 && Random.nextBoolean()) { // 최대 5개까지만 추가
                    likedPairs.add(Pair(CURRENT_USER_ID, user.id))
                    dummySentLikes.add(createLikeItem(user, LikeRepository.SENT))
                }
                // 현재 사용자가 다른 사용자로부터 좋아요를 받음
                // 받은 좋아요는 순서를 고정하기 위해 고정된 시간을 부여합니다.
                if (Random.nextBoolean()) {
                    likedPairs.add(Pair(user.id, CURRENT_USER_ID))
                    val fixedReceivedTime = initialReceivedTime - (index * 10000L) // 인덱스를 기반으로 시간 고정 (예: 10초 간격)
                    dummyReceivedLikes.add(createLikeItem(user, LikeRepository.RECEIVED, fixedReceivedTime))
                }
            }
        }
        allLikes[LikeRepository.RECEIVED] = dummyReceivedLikes.toMutableList()
        allLikes[LikeRepository.SENT] = dummySentLikes.toMutableList()
    }

    override suspend fun getLikes(tab: String, cursor: Long?, limit: Int): Result<LikeResult> {
        delay(500)

        // 탭에 따라 필터링된 좋아요 데이터를 가져옵니다.
        val data = allLikes[tab]?.sortedByDescending { it.receivedTime } ?: emptyList()

        val startIndex = if (cursor == null) 0 else {
            data.indexOfFirst { it.receivedTime < cursor } // cursor보다 작은 첫번째 index
        }

        if (startIndex == -1 || startIndex >= data.size) {
            return Result.success(LikeResult(emptyList(), null))
        }

        val endIndex = (startIndex + limit).coerceAtMost(data.size)
        val newData = data.subList(startIndex, endIndex)

        val nextCursor = newData.lastOrNull()?.receivedTime

        return Result.success(LikeResult(newData, nextCursor))
    }

    override suspend fun likeUser(userId: String): ResultWrapper<Unit> {
        return try {
            // TODO: 실제 API 호출 구현
            delay(500) // API 호출 시뮬레이션
            likedPairs.add(Pair(CURRENT_USER_ID, userId)) // 현재 사용자가 userId에게 좋아요를 보냄

            // 좋아요를 보낸 아이템을 allLikes에 추가 (receivedTime을 최신으로)
            val likedUserDetail = _users.find { it.id == userId }
            likedUserDetail?.let { user ->
                val newLikeItem = createLikeItem(user, LikeRepository.SENT, System.currentTimeMillis())
                allLikes[LikeRepository.SENT]?.add(newLikeItem)
            }

            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            ResultWrapper.Error(ErrorType.ExceptionError(e.message ?: "좋아요 추가 중 오류가 발생했습니다."))
        }
    }

    override suspend fun isUserLiked(userId: String): ResultWrapper<Boolean> {
        delay(100) // Simulate network delay
        // 더미 구현: 현재 사용자가 userId에게 좋아요를 보냈는지 확인
        return ResultWrapper.Success(likedPairs.contains(Pair(CURRENT_USER_ID, userId)))
    }

    // LikeItem을 생성하는 헬퍼 함수
    private fun createLikeItem(userDetail: UserDetail, tab: String, explicitReceivedTime: Long? = null): LikeItem {
        val now = System.currentTimeMillis()
        // receivedTime은 받은 시각이므로, 좋아요를 받은 경우에만 의미가 있음
        val receivedTime = explicitReceivedTime ?: now

        return LikeItemData(
            id = userDetail.id,
            thumbnail = userDetail.thumbnailUrl,
            nickname = userDetail.name,
            age = userDetail.age,
            introduction = userDetail.introduction,
            receivedTime = receivedTime,
            location = userDetail.location,
            occupation = userDetail.occupation,
            likedAt = now // 좋아요를 누른 시각
        ).toDomain()
    }
}