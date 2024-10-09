package com.example.tokitoki.data.repository

import com.example.tokitoki.data.local.CategoryDao
import com.example.tokitoki.domain.converter.CategoryConverter
import com.example.tokitoki.domain.model.Category
import com.example.tokitoki.domain.repository.CategoryRepository

class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao
) : CategoryRepository {
    override suspend fun getCategories(): List<Category> {
        val categoryEntities = categoryDao.getCategories()
        return categoryEntities.map { CategoryConverter.dataToDomain(it) }
    }
}