package com.example.tokitoki.ui.converter

import com.example.tokitoki.data.model.TagType
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

    fun tagTypeToUi(tagType: TagType): CategoryItem {
        return CategoryItem(
            id = tagType.ordinal, // TagType의 순서 값을 ID로 사용
            title = tagType.value  // TagType의 문자열 값을 title로 사용
        )
    }
}