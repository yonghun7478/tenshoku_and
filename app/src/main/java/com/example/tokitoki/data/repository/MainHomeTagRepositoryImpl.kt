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
        // TagRepository에서 모든 태그를 가져와 첫 번째 태그를 "오늘의 태그"로 반환합니다.
        return tagRepository.getAllTags().map { tags ->
            tags.first()
        }
    }

    override suspend fun getTrendingTags(): Result<List<MainHomeTag>> {
        // TagRepository에서 모든 태그를 가져와 구독자 수 기준으로 정렬하여 상위 10개를 "인기 태그"로 반환합니다.
        return tagRepository.getAllTags().map { tags ->
            tags.sortedByDescending { it.subscriberCount }.take(10)
        }
    }

    override suspend fun getMyTags(): Result<List<MainHomeTag>> {
        // TagRepository를 통해 현재 사용자가 구독한 태그 목록을 가져옵니다.
        return tagRepository.getAllTags().map { allTags ->
            allTags.filter { tagRepository.isTagSubscribed(it.id) }
        }
    }

    override suspend fun getSuggestedTags(): Result<List<MainHomeTag>> {
        // TagRepository에서 모든 태그를 가져와 그 중 일부를 "추천 태그"로 반환합니다. (현재는 랜덤 10개)
        return tagRepository.getAllTags().map { tags ->
            tags.shuffled().take(10)
        }
    }

    override suspend fun getTagsByCategory(categoryId: String): Result<List<MainHomeTag>> {
        // 특정 카테고리의 태그들을 TagRepository에서 가져옵니다. (이 기능은 TagRepository에 추가 필요)
        // 임시로 전체 태그에서 필터링합니다.
        return tagRepository.getAllTags().map { tags ->
            tags.filter { it.categoryId == categoryId }
        }
    }

    override suspend fun getTagsByQuery(query: String): Result<List<MainHomeTag>> {
        // 특정 쿼리로 태그를 검색합니다. (이 기능은 TagRepository에 추가 필요)
        // 임시로 전체 태그에서 필터링합니다.
        return tagRepository.getAllTags().map { tags ->
            tags.filter { it.name.contains(query, ignoreCase = true) }
        }
    }

    override suspend fun getMyTagsByType(userId: String, tagType: TagType): Result<List<MainHomeTag>> {
        return try {
            delay(500)
            // 현재는 임시로 myTags에서 타입별로 필터링
            // TODO: 실제 구현에서는 userId를 사용하여 해당 사용자의 마이태그만 필터링해야 함
            val allTags = tagRepository.getAllTags().getOrThrow()
            val filtered = allTags.filter { it.tagType == tagType }.take(5) // 임시로 5개만 반환
            Result.success(filtered)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTagDetail(tagId: String): Result<MainHomeTag> {
        return try {
            delay(500)
            val tag = tagRepository.getAllTags().getOrThrow().find { it.id == tagId }
            if (tag != null) {
                Result.success(tag)
            } else {
                Result.failure(Exception("Tag not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserTags(userId: String): Result<List<MainHomeTag>> {
        // 더미 데이터: 사용자의 모든 태그를 가져오는 로직 (myTags 리스트 사용)
        // TODO: 실제 API 구현 시 해당 사용자의 태그 목록을 가져오도록 수정 필요
        delay(200) // API 호출 시뮬레이션을 위한 약간의 딜레이
        val allTags = tagRepository.getAllTags().getOrThrow()
        val myTags = listOf(
            allTags[7],   // 写真撮影
            allTags[8],   // アート好き
            allTags[9],   // ガーデニング
            allTags[10],
            allTags[11]
        )
        return Result.success(myTags)
    }
}