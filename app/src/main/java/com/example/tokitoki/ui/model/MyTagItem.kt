package com.example.tokitoki.ui.model

data class MyTagItem(
    val tagId: Int,
    val categoryId: Int,
    val title: String = "",
    val url: String = "",
    val categoryTitle: String = "",
)