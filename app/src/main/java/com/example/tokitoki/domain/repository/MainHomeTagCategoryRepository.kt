package com.example.tokitoki.domain.repository

import com.example.tokitoki.domain.model.MainHomeTagCategory

interface MainHomeTagCategoryRepository {
    suspend fun getCategories(): Result<List<MainHomeTagCategory>>
} 