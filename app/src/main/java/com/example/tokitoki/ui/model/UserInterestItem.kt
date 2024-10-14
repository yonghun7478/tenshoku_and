package com.example.tokitoki.ui.model

data class UserInterestItem(
    val id: Int = 0,
    val categoryId: Int = 0,
    val title: String = "",
    val url: String = "",
    val desc: String = "",
    val showBadge: Boolean = false,
)
