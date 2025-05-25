package com.example.tokitoki.data.model

import com.example.tokitoki.domain.model.MainHomeTagCategory

data class MainHomeTagCategoryData(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String
) {
    fun toDomain(): MainHomeTagCategory = MainHomeTagCategory(
        id = this.id,
        name = this.name,
        description = this.description,
        imageUrl = this.imageUrl
    )
} 