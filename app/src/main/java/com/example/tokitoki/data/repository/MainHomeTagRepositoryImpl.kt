package com.example.tokitoki.data.repository

import com.example.tokitoki.data.model.MainHomeTagData
import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.repository.MainHomeTagRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class MainHomeTagRepositoryImpl @Inject constructor() : MainHomeTagRepository {

    private val todayTag = MainHomeTagData("오늘의 태그", "today_image", 100).toDomain()
    private val trendingTags = listOf(
        MainHomeTagData("트렌딩 태그1", "image1", 50),
        MainHomeTagData("트렌딩 태그2", "image2", 120),
        MainHomeTagData("트렌딩 태그3", "image3", 80)
    ).map { it.toDomain() }
    private val myTags = listOf(
        MainHomeTagData("선택한 태그1", "my_image1", 30),
        MainHomeTagData("선택한 태그2", "my_image2", 45),
    ).map { it.toDomain() }

    private val suggestedTags = listOf(
        MainHomeTagData("추천 태그1", "suggested_image1", 15),
        MainHomeTagData("추천 태그2", "suggested_image2", 33),
        MainHomeTagData("추천 태그3", "suggested_image3", 77),
        MainHomeTagData("추천 태그4", "suggested_image3", 77),
        MainHomeTagData("추천 태그5", "suggested_image3", 77),
        MainHomeTagData("추천 태그6", "suggested_image3", 77),
        MainHomeTagData("추천 태그7", "suggested_image3", 77),
        MainHomeTagData("추천 태그8", "suggested_image3", 77),
    ).map { it.toDomain() }


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
                MainHomeTagData("검색결과1_$query", "search_result_image1", 10),
                MainHomeTagData("검색결과2_$query", "search_result_image2", 20)
            ).map { it.toDomain() }
        } else {
            listOf() // 빈 검색어면 빈 결과
        }
        return Result.success(results)
    }

    override suspend fun getRecentSearches(): Result<List<MainHomeTag>> {
        delay(200)
        return Result.success(recentSearches.map { it.toDomain() })
    }

    override suspend fun addRecentSearch(tags: List<MainHomeTag>): Result<Unit> {
        delay(100)
        recentSearches = (tags.map { it.toData() } + recentSearches) // 리스트 + 리스트
            .distinctBy { it.name }
            .take(5)
            .toMutableList()
        return Result.success(Unit)
    }

    override suspend fun deleteRecentSearch(tag: MainHomeTag): Result<Unit> {
        TODO("Not yet implemented")
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
        return Result.success(selectedTags.toList()) // Return a copy
    }

    override suspend fun saveTempSelectedTags(tags: List<MainHomeTag>) {
        tempSelectedTags = tags.toList() // Make a copy
    }

    override suspend fun restoreTempSelectedTags(): Result<List<MainHomeTag>> {
        return Result.success(tempSelectedTags)
    }
}