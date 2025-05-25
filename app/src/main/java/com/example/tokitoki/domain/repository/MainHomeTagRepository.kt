package com.example.tokitoki.domain.repository

import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.model.MainHomeTagDetail
import com.example.tokitoki.domain.model.MainHomeTagSubscriber

interface MainHomeTagRepository {
    suspend fun getTodayTag(): Result<MainHomeTag>
    suspend fun getTrendingTags(): Result<List<MainHomeTag>>
    suspend fun getMyTags(): Result<List<MainHomeTag>>
    suspend fun getSuggestedTags(): Result<List<MainHomeTag>>
}