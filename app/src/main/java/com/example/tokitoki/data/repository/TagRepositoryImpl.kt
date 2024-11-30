package com.example.tokitoki.data.repository

import com.example.tokitoki.data.local.CategoryDao
import com.example.tokitoki.data.local.TagDao
import com.example.tokitoki.domain.converter.CategoryConverter
import com.example.tokitoki.domain.converter.TagConverter
import com.example.tokitoki.domain.model.Category
import com.example.tokitoki.domain.model.Tag
import com.example.tokitoki.domain.repository.TagRepository
import javax.inject.Inject

class TagRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val tagDao: TagDao
) : TagRepository {

    override suspend fun getCategories(): List<Category> {
        return categoryDao.getCategories().map { CategoryConverter.dataToDomain(it) }
    }

    override suspend fun getTags(categoryId: Int): List<Tag> {
        return tagDao.getTagsWithCategory(categoryId).map { TagConverter.dataToDomain(it) }
    }

    override suspend fun getTags(tagIds: List<Int>): List<Tag> {
        return tagDao.getTagsByIds(tagIds).map { TagConverter.dataToDomain(it) }
    }
}