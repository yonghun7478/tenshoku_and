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
    private val allHomeTags: List<MainHomeTag> by lazy { DummyData.getHomeTags() }

    // 2. (사용자 ID, 태그 ID)로 구독 관계만 저장
    private val subscriptions = mutableSetOf<Pair<String, String>>()

    // 현재 로그인한 사용자는 "1"이라고 가정
    private val currentUserId = "0"

    init {
        // 더미 구독 관계 생성
        subscriptions.add(currentUserId to "104") // #개발자
        subscriptions.add(currentUserId to "106") // #영화광
        subscriptions.add("2" to "102")           // #맛집탐방
        subscriptions.add("3" to "104")           // #개발자
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

    override fun isTagSubscribed(tagId: String): Boolean {
        return subscriptions.contains(currentUserId to tagId)
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

    override suspend fun subscribeTag(tagId: String): Result<Unit> {
        return try {
            subscriptions.add(currentUserId to tagId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun unsubscribeTag(tagId: String): Result<Unit> {
        return try {
            subscriptions.remove(currentUserId to tagId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}