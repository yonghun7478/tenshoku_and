package com.example.tokitoki.data.repository

import com.example.tokitoki.domain.model.TagType
import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.repository.MainHomeTagRepository
import com.example.tokitoki.domain.repository.TagRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class MainHomeTagRepositoryImpl @Inject constructor(
    private val tagRepository: TagRepository
) : MainHomeTagRepository {

    override suspend fun getTodayTag(): Result<MainHomeTag> {
        // TagRepository에서 모든 태그를 가져와, 사용자가 아직 구독하지 않은 태그 중 첫 번째 태그를 "오늘의 태그"로 반환합니다.
        return tagRepository.getAllTags().map { tags ->
            // 모든 태그 중 첫 번째 태그를 가져오고, 해당 태그의 구독 상태를 설정합니다.
            val todayTag = tags.first()
            todayTag.copy(isSubscribed = tagRepository.isTagSubscribed(todayTag.id))
        }
    }

    override suspend fun getTrendingTags(): Result<List<MainHomeTag>> {
        // TagRepository에서 모든 태그를 가져와, 사용자가 아직 구독하지 않은 태그들 중에서
        // 구독자 수 기준으로 정렬하여 상위 10개를 "인기 태그"로 반환합니다.
        return tagRepository.getAllTags().map { tags ->
            // 모든 태그를 가져와 구독자 수 기준으로 정렬하고, 각 태그의 구독 상태를 설정합니다.
            tags.sortedByDescending { it.subscriberCount }
                .take(5)
                .map { it.copy(isSubscribed = tagRepository.isTagSubscribed(it.id)) }
        }
    }

    override suspend fun getSuggestedTags(): Result<List<MainHomeTag>> {
        // TagRepository에서 모든 태그를 가져와, 사용자가 아직 구독하지 않은 태그들 중에서
        // 그 중 일부를 "추천 태그"로 반환합니다. (현재는 랜덤 10개)
        return tagRepository.getAllTags().map { tags ->
            // 모든 태그를 가져와 랜덤 10개를 반환하고 각 태그의 구독 상태를 설정합니다.
            tags.shuffled().take(10).map { tag ->
                tag.copy(isSubscribed = tagRepository.isTagSubscribed(tag.id)) // 구독 여부에 따라 isSubscribed 설정
            }
        }
    }

    override suspend fun getTagsByCategory(categoryId: String): Result<List<MainHomeTag>> {
        // 특정 카테고리의 태그들을 가져와 각 태그의 구독 상태를 설정합니다.
        return tagRepository.getAllTags().map { tags ->
            tags.filter { it.categoryId == categoryId }
                .map { tag ->
                    tag.copy(isSubscribed = tagRepository.isTagSubscribed(tag.id))
                }
        }
    }

    override suspend fun getTagsByQuery(query: String): Result<List<MainHomeTag>> {
        // 특정 쿼리로 태그를 검색하고 각 태그의 구독 상태를 설정합니다.
        return tagRepository.getAllTags().map { tags ->
            tags.filter { it.name.contains(query, ignoreCase = true) }
                .map { tag ->
                    tag.copy(isSubscribed = tagRepository.isTagSubscribed(tag.id))
                }
        }
    }

    override suspend fun getMyTagsByType(userId: String, tagType: TagType): Result<List<MainHomeTag>> {
        // 사용자의 태그를 타입별로 필터링하고, 구독 상태를 true로 설정합니다.
        return try {
            delay(500)
            val allTags = tagRepository.getAllTags().getOrThrow()
            val mySubscribedTags = allTags.filter { tagRepository.isTagSubscribed(it.id) }
            val filtered = mySubscribedTags.filter { it.tagType == tagType }.take(5)
            Result.success(filtered.map { it.copy(isSubscribed = true) })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTagDetail(tagId: String): Result<MainHomeTag> {
        // 태그 상세 정보를 가져오고 구독 상태를 설정합니다.
        return try {
            delay(500)
            val tag = tagRepository.getAllTags().getOrThrow().find { it.id == tagId }
            if (tag != null) {
                Result.success(tag.copy(isSubscribed = tagRepository.isTagSubscribed(tag.id)))
            } else {
                Result.failure(Exception("Tag not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserTags(userId: String): Result<List<MainHomeTag>> {
        // 특정 사용자의 태그 목록을 가져옵니다. TagRepository에서 해당 userId가 구독한 태그를 직접 가져옵니다.
        return tagRepository.getUserSubscribedTags(userId)
    }
}