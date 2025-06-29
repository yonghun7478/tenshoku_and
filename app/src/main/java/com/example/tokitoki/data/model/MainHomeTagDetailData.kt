package com.example.tokitoki.data.model

import com.example.tokitoki.domain.model.MainHomeTagDetail

data class MainHomeTagDetailData(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val subscriberCount: Int
)

fun MainHomeTagDetailData.toDomain(): MainHomeTagDetail {
    return MainHomeTagDetail(
        id = id,
        name = name,
        description = description,
        imageUrl = imageUrl,
        subscriberCount = subscriberCount
    )
} 