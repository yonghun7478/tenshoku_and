package com.example.tokitoki.domain.model

data class UserInterest(
    val id: Int,
    val title: String,
    val url: String,
    val desc: String,
    val categoryId: Int,
    val categoryTitle: String
)
