package com.example.tokitoki.data.repository

import com.example.tokitoki.data.model.MainHomeTagData
import com.example.tokitoki.data.model.MainHomeTagDetailData
import com.example.tokitoki.data.model.MainHomeTagSubscriberData
import com.example.tokitoki.data.model.toDomain
import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.model.MainHomeTagDetail
import com.example.tokitoki.domain.model.MainHomeTagSubscriber
import com.example.tokitoki.domain.repository.MainHomeTagRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class MainHomeTagRepositoryImpl @Inject constructor() : MainHomeTagRepository {

    private val todayTag = MainHomeTagData(
        id = "today_1",
        name = "오늘의 태그",
        description = "오늘 가장 인기있는 태그입니다.",
        imageUrl = "today_image",
        subscriberCount = 100
    ).toDomain()

    private val trendingTags = listOf(
        MainHomeTagData(
            id = "trend_1",
            name = "트렌딩 태그1",
            description = "현재 가장 인기있는 태그입니다.",
            imageUrl = "image1",
            subscriberCount = 50
        ),
        MainHomeTagData(
            id = "trend_2",
            name = "트렌딩 태그2",
            description = "두 번째로 인기있는 태그입니다.",
            imageUrl = "image2",
            subscriberCount = 120
        )
    ).map { it.toDomain() }

    private val myTags = listOf(
        MainHomeTagData(
            id = "my_1",
            name = "선택한 태그1",
            description = "내가 선택한 첫 번째 태그입니다.",
            imageUrl = "my_image1",
            subscriberCount = 30
        ),
        MainHomeTagData(
            id = "my_2",
            name = "선택한 태그2",
            description = "내가 선택한 두 번째 태그입니다.",
            imageUrl = "my_image2",
            subscriberCount = 45
        )
    ).map { it.toDomain() }

    private val suggestedTags = listOf(
        MainHomeTagData(
            id = "suggest_1",
            name = "추천 태그1",
            description = "첫 번째 추천 태그입니다.",
            imageUrl = "suggested_image1",
            subscriberCount = 15
        ),
        MainHomeTagData(
            id = "suggest_2",
            name = "추천 태그2",
            description = "두 번째 추천 태그입니다.",
            imageUrl = "suggested_image2",
            subscriberCount = 33
        )
    ).map { it.toDomain() }

    private val tagDetails = mapOf(
        "today_1" to MainHomeTagDetailData(
            id = "today_1",
            name = "오늘의 태그",
            description = "오늘 가장 인기있는 태그입니다.",
            imageUrl = "today_image",
            subscriberCount = 100
        ),
        "trend_1" to MainHomeTagDetailData(
            id = "trend_1",
            name = "트렌딩 태그1",
            description = "현재 가장 인기있는 태그입니다.",
            imageUrl = "image1",
            subscriberCount = 50
        )
    )

    private val tagSubscribers = mapOf(
        "today_1" to listOf(
            MainHomeTagSubscriberData(
                userId = "user1",
                profileImageUrl = "profile1",
                age = 25,
                location = "서울시 강남구"
            ),
            MainHomeTagSubscriberData(
                userId = "user2",
                profileImageUrl = "profile2",
                age = 30,
                location = "서울시 서초구"
            )
        ),
        "trend_1" to listOf(
            MainHomeTagSubscriberData(
                userId = "user3",
                profileImageUrl = "profile3",
                age = 28,
                location = "서울시 송파구"
            )
        )
    )

    private var recentSearches: List<MainHomeTagData> = listOf()
    private var selectedTags: MutableList<MainHomeTag> = mutableListOf()
    private var tempSelectedTags: List<MainHomeTag> = listOf()

    override suspend fun getTodayTag(): Result<MainHomeTag> {
        delay(500)
        return Result.success(todayTag)
    }

    override suspend fun getTrendingTags(): Result<List<MainHomeTag>> {
        delay(700)
        return Result.success(trendingTags)
    }

    override suspend fun getMyTags(): Result<List<MainHomeTag>> {
        delay(300)
        return Result.success(myTags)
    }

    override suspend fun getSuggestedTags(): Result<List<MainHomeTag>> {
        delay(400)
        return Result.success(suggestedTags)
    }

    override suspend fun searchTags(query: String): Result<List<MainHomeTag>> {
        delay(200)
        val results = if (query.isNotBlank()) {
            listOf(
                MainHomeTagData(
                    id = "search_1",
                    name = "검색결과1_$query",
                    description = "검색 결과 태그입니다.",
                    imageUrl = "search_result_image1",
                    subscriberCount = 10
                ),
                MainHomeTagData(
                    id = "search_2",
                    name = "검색결과2_$query",
                    description = "검색 결과 태그입니다.",
                    imageUrl = "search_result_image2",
                    subscriberCount = 20
                )
            ).map { it.toDomain() }
        } else {
            listOf()
        }
        return Result.success(results)
    }

    override suspend fun getRecentSearches(): Result<List<MainHomeTag>> {
        delay(200)
        return Result.success(recentSearches.map { it.toDomain() })
    }

    override suspend fun addRecentSearch(tags: List<MainHomeTag>): Result<Unit> {
        delay(100)
        recentSearches = (tags.map { it.toData() } + recentSearches)
            .distinctBy { it.name }
            .take(5)
            .toMutableList()
        return Result.success(Unit)
    }

    override suspend fun deleteRecentSearch(tag: MainHomeTag): Result<Unit> {
        delay(100)
        recentSearches = recentSearches.filter { it.name != tag.name }
        return Result.success(Unit)
    }

    override suspend fun addSelectedTag(tag: MainHomeTag): Result<Unit> {
        delay(100)
        selectedTags.add(tag)
        return Result.success(Unit)
    }

    override suspend fun removeSelectedTag(tag: MainHomeTag): Result<Unit> {
        delay(100)
        selectedTags.remove(tag)
        return Result.success(Unit)
    }

    override suspend fun getSelectedTags(): Result<List<MainHomeTag>> {
        delay(100)
        return Result.success(selectedTags.toList())
    }

    override suspend fun saveTempSelectedTags(tags: List<MainHomeTag>) {
        tempSelectedTags = tags.toList()
    }

    override suspend fun restoreTempSelectedTags(): Result<List<MainHomeTag>> {
        return Result.success(tempSelectedTags)
    }

    override suspend fun getTagDetail(tagId: String): Result<MainHomeTagDetail> {
        delay(500)
        return tagDetails[tagId]?.let {
            Result.success(it.toDomain())
        } ?: Result.failure(IllegalArgumentException("Tag not found"))
    }

    override suspend fun getTagSubscribers(tagId: String): Result<List<MainHomeTagSubscriber>> {
        delay(300)
        return tagSubscribers[tagId]?.let {
            Result.success(it.map { subscriber -> subscriber.toDomain() })
        } ?: Result.success(emptyList())
    }
}