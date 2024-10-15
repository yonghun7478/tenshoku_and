package com.example.tokitoki.data.repository

import com.example.tokitoki.data.local.CategoryDao
import com.example.tokitoki.data.local.CategoryEntity
import com.example.tokitoki.domain.converter.CategoryConverter
import com.example.tokitoki.domain.model.Category
import com.example.tokitoki.domain.repository.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {
    override suspend fun getCategories(): List<Category> {
//        val categoryEntities = categoryDao.getCategories()

        val categoryEntities = listOf(
            CategoryEntity(id = 1, title = "趣味"),          // "Hobby"
            CategoryEntity(id = 2, title = "ライフスタイル"),  // "Lifestyle"
            CategoryEntity(id = 3, title = "価値観")          // "Values"
        )
        return categoryEntities.map { CategoryConverter.dataToDomain(it) }
    }
}