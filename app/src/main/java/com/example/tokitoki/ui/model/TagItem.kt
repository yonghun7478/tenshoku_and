package com.example.tokitoki.ui.model

data class TagItem(
    val id: Int = 0,
    val tagTypeId: Int = 0,
    val title: String = "",
    val url: String = "",
    val desc: String = "",
    val showBadge: Boolean = false,
)
