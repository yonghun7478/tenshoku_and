package com.example.tokitoki.data.repository

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.data.dummy.DummyData
import com.example.tokitoki.domain.model.IdList
import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.model.TagType
import com.example.tokitoki.domain.model.User
import com.example.tokitoki.domain.model.UserList
import com.example.tokitoki.domain.repository.TagRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TagRepositoryImpl @Inject constructor() : TagRepository {

    // 1. 모든 MainHomeTag 원본 데이터를 DummyData를 통해 생성하고 보관
    private var allHomeTags: List<MainHomeTag>

    // 2. (사용자 ID, 태그 ID)로 구독 관계만 저장
    private val subscriptions = mutableSetOf<Pair<String, String>>()

    init {
        // 기존 구독 데이터를 모두 제거하고 새로운 구독 데이터를 추가합니다.
        // 각 태그별로 4명 정도의 임의의 사용자를 구독자로 설정합니다.
        val random = java.util.Random()
        (101..115).forEach { tagIdInt ->
            val tagId = tagIdInt.toString()
            val numberOfSubscribers = random.nextInt(7) + 4 // 4 (inclusive) to 10 (inclusive)
            repeat(numberOfSubscribers) {
                val userId = (random.nextInt(150) + 1).toString() // 1부터 150 사이의 임의의 유저 ID
                subscriptions.add(userId to tagId)
            }
        }

        // DummyData에서 초기 태그 목록을 가져옵니다.
        val initialHomeTags = DummyData.getHomeTags()

        // 각 태그별 구독자 수를 계산합니다.
        val tagSubscriberCounts = subscriptions.groupBy { it.second }.mapValues { it.value.size }

        // initialHomeTags에 subscriberCount를 업데이트합니다.
        allHomeTags = initialHomeTags.map { tag ->
            tag.copy(subscriberCount = tagSubscriberCounts[tag.id] ?: 0)
        }
    }

    override suspend fun getTagTypeList(): List<TagType> {
        return TagType.entries.toList()
    }

    override suspend fun getTagsByType(tagType: TagType): Result<List<MainHomeTag>> {
        val filteredTags = allHomeTags.filter { it.tagType == tagType }
        return Result.success(filteredTags)
    }

    override suspend fun getTags(tagIds: List<Int>): List<MainHomeTag> {
        val stringIds = tagIds.map { it.toString() }
        return allHomeTags.filter { stringIds.contains(it.id) }
    }

    override suspend fun getAllTags(): Result<List<MainHomeTag>> {
        return Result.success(allHomeTags)
    }

    override suspend fun getTagSubscribers(
        tagId: String,
        cursor: String?,
        limit: Int
    ): ResultWrapper<IdList> {
        return try {
            // 이 태그를 구독하는 모든 사용자의 ID를 찾음
            val allUserIds = subscriptions.filter { it.second == tagId }.map { it.first }

            // ID를 기준으로 정렬하여 일관된 순서 보장
            val sortedUserIds = allUserIds.sorted()

            val startIndex = cursor?.let { c -> sortedUserIds.indexOf(c) + 1 } ?: 0
            val pagedIds = sortedUserIds.drop(startIndex).take(limit)

            val nextCursor = pagedIds.lastOrNull()
            val isLastPage = (startIndex + pagedIds.size) >= sortedUserIds.size

            ResultWrapper.Success(
                IdList(
                    ids = pagedIds,
                    nextCursor = if (isLastPage) null else nextCursor,
                    isLastPage = isLastPage
                )
            )
        } catch (e: Exception) {
            ResultWrapper.Error(ResultWrapper.ErrorType.ExceptionError(e.message ?: "Unknown error"))
        }
    }

    override suspend fun getUserSubscribedTags(userId: String): Result<List<MainHomeTag>> {
        return try {
            // 특정 userId가 구독한 태그 ID들을 subscriptions 셋에서 찾습니다.
            val subscribedTagIds = subscriptions.filter { it.first == userId }.map { it.second }.toSet()

            // 모든 태그 중에서 해당 ID를 가진 태그들을 필터링합니다.
            val userSubscribedTags = allHomeTags.filter { subscribedTagIds.contains(it.id) }

            // 필터링된 태그들의 isSubscribed를 true로 설정하여 반환합니다.
            Result.success(userSubscribedTags.map { it.copy(isSubscribed = true) })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}