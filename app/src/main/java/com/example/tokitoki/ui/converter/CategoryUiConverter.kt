package com.example.tokitoki.ui.converter

import com.example.tokitoki.domain.model.Category
import com.example.tokitoki.ui.model.CategoryItem

object CategoryUiConverter {
    fun domainToUi(category: Category): CategoryItem {
        return CategoryItem(
            id = category.id,
            title = category.title
        )
    }

    fun uiToDomain(categoryItem: CategoryItem): Category {
        return Category(
            id = categoryItem.id,
            title = categoryItem.title
        )
    }
}