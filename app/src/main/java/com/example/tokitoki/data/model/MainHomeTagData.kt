package com.example.tokitoki.data.model

import com.example.tokitoki.domain.model.MainHomeTag

data class MainHomeTagData(
    val name: String,
    val imageUrl: String,
    val userCount: Int
) {
    fun toDomain(): MainHomeTag = MainHomeTag(
        name = this.name,
        imageUrl = this.imageUrl,
        userCount = this.userCount
    )
}