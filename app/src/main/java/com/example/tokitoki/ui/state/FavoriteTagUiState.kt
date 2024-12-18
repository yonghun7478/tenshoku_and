package com.example.tokitoki.ui.state

import com.example.tokitoki.ui.model.CategoryItem
import com.example.tokitoki.ui.model.TagItem

data class FavoriteTagUiState(
    val tagsByCategory: Map<String, List<TagItem>> = emptyMap(),
    val categoryList: List<CategoryItem> = listOf()
)