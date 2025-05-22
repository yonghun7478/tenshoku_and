package com.example.tokitoki.domain.repository

import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.model.MainHomeTagDetail
import com.example.tokitoki.domain.model.MainHomeTagSubscriber

interface MainHomeTagRepository {
    suspend fun getTodayTag(): Result<MainHomeTag>
    suspend fun getTrendingTags(): Result<List<MainHomeTag>>
    suspend fun getMyTags(): Result<List<MainHomeTag>>
    suspend fun getSuggestedTags(): Result<List<MainHomeTag>>
    suspend fun searchTags(query: String): Result<List<MainHomeTag>>
    suspend fun getRecentSearches(): Result<List<MainHomeTag>>
    suspend fun addRecentSearch(tags: List<MainHomeTag>): Result<Unit>
    suspend fun deleteRecentSearch(tag: MainHomeTag): Result<Unit>
    suspend fun addSelectedTag(tag: MainHomeTag): Result<Unit>
    suspend fun removeSelectedTag(tag: MainHomeTag): Result<Unit>
    suspend fun getSelectedTags(): Result<List<MainHomeTag>>

    //임시저장 관련
    suspend fun saveTempSelectedTags(tags: List<MainHomeTag>)
    suspend fun restoreTempSelectedTags(): Result<List<MainHomeTag>>

    // 새로운 메서드들
    suspend fun getTagDetail(tagId: String): Result<MainHomeTagDetail>
    suspend fun getTagSubscribers(tagId: String): Result<List<MainHomeTagSubscriber>>
}