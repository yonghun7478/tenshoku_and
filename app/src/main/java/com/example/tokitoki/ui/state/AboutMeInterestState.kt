package com.example.tokitoki.ui.state

import com.example.tokitoki.ui.model.CategoryItem
import com.example.tokitoki.ui.model.UserInterestItem

data class AboutMeInterestState(
    val showDialog: Boolean = false,
    val userInterestsByCategory: Map<String, List<UserInterestItem>> = emptyMap(),
    val categoryList: List<CategoryItem> = listOf()
)
