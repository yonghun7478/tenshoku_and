package com.example.tokitoki.data.repository

import com.example.tokitoki.data.local.CategoryDao
import com.example.tokitoki.data.local.TagDao
import com.example.tokitoki.domain.converter.CategoryConverter
import com.example.tokitoki.domain.converter.TagConverter
import com.example.tokitoki.domain.model.Category
import com.example.tokitoki.domain.model.Tag
import com.example.tokitoki.domain.repository.TagRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TagRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val tagDao: TagDao
) : TagRepository {

    override suspend fun getCategories(): List<Category> {
        return withContext(Dispatchers.IO) {
            categoryDao.getCategories().map { CategoryConverter.dataToDomain(it) }
        }
    }

    override suspend fun getTags(categoryId: Int): List<Tag> {
        return withContext(Dispatchers.IO) {
            tagDao.getTagsWithCategoryAndTagId(categoryId).map { TagConverter.dataToDomain(it) }
        }
    }

    override suspend fun getTags(categoryId: Int, tagIds: List<Int>): List<Tag> {
        return withContext(Dispatchers.IO) {
            tagDao.getTagsWithCategoryAndTagId(categoryId, tagIds).map { TagConverter.dataToDomain(it) }
        }
    }
}