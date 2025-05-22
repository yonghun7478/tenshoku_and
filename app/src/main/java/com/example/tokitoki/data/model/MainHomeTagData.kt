package com.example.tokitoki.data.model

import com.example.tokitoki.domain.model.MainHomeTag

data class MainHomeTagData(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val subscriberCount: Int
)

fun MainHomeTagData.toDomain(): MainHomeTag {
    return MainHomeTag(
        id = id,
        name = name,
        description = description,
        imageUrl = imageUrl,
        subscriberCount = subscriberCount
    )
}

fun MainHomeTag.toData(): MainHomeTagData {
    return MainHomeTagData(
        id = id,
        name = name,
        description = description,
        imageUrl = imageUrl,
        subscriberCount = subscriberCount
    )
}