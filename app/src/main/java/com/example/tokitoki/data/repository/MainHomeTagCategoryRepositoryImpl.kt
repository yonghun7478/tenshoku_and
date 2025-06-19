package com.example.tokitoki.data.repository

import com.example.tokitoki.data.dummy.DummyData
import com.example.tokitoki.data.model.MainHomeTagCategoryData
import com.example.tokitoki.domain.model.MainHomeTagCategory
import com.example.tokitoki.domain.repository.MainHomeTagCategoryRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class MainHomeTagCategoryRepositoryImpl @Inject constructor() : MainHomeTagCategoryRepository {

    override suspend fun getCategories(): Result<List<MainHomeTagCategory>> {
        return try {
            // API 호출을 시뮬레이션하기 위한 딜레이
            delay(1000)
            Result.success(DummyData.dummyMainHomeTagCategories.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 