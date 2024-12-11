package com.example.tokitoki.domain.repository

import com.example.tokitoki.domain.model.Category
import com.example.tokitoki.domain.model.Tag


interface TagRepository {
    suspend fun getCategories(): List<Category>
    suspend fun getTags(categoryId : Int): List<Tag>
    suspend fun getTags(tagIds : List<Int>): List<Tag>
}