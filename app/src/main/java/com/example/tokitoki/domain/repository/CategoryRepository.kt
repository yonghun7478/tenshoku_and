package com.example.tokitoki.domain.repository

import com.example.tokitoki.domain.model.Category


interface CategoryRepository {
    suspend fun getCategories(): List<Category>
}
