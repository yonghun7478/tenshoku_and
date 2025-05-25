package com.example.tokitoki.ui.state

data class TagCategoryUiState(
    val id: String,
    val name: String,
    val imageUrl: String
)

data class TagResultUiState(
    val id: String,
    val name: String,
    val imageUrl: String,
    val subscriberCount: Int
) 