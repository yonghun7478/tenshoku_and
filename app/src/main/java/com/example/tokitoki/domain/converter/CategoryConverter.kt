package com.example.tokitoki.domain.converter

import com.example.tokitoki.data.local.CategoryEntity
import com.example.tokitoki.domain.model.Category

object CategoryConverter {
    fun dataToDomain(categoryEntity: CategoryEntity): Category {
        return Category(
            id = categoryEntity.id,
            title = categoryEntity.title
        )
    }
}