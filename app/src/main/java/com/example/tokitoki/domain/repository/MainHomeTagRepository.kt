package com.example.tokitoki.domain.repository

import com.example.tokitoki.domain.model.TagType
import com.example.tokitoki.domain.model.MainHomeTag

interface MainHomeTagRepository {
    suspend fun getTodayTag(): Result<MainHomeTag>
    suspend fun getTrendingTags(): Result<List<MainHomeTag>>
    suspend fun getSuggestedTags(): Result<List<MainHomeTag>>
    suspend fun getTagsByCategory(categoryId: String): Result<List<MainHomeTag>>
    suspend fun getTagsByQuery(query: String): Result<List<MainHomeTag>>
    suspend fun getMyTagsByType(userId: String, tagType: TagType): Result<List<MainHomeTag>>
    suspend fun getTagDetail(tagId: String): Result<MainHomeTag>
    suspend fun getUserTags(userId: String): Result<List<MainHomeTag>>
}