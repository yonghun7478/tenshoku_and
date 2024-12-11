package com.example.tokitoki.domain.model

data class Tag(
    val id: Int,
    val title: String,
    val url: String,
    val categoryId: Int,
    val categoryTitle: String
)
