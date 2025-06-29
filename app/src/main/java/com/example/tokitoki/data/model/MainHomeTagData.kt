package com.example.tokitoki.data.model

import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.model.TagType

data class MainHomeTagData(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val subscriberCount: Int,
    val categoryId: String,
    val tagType: TagType,
    val isSubscribed: Boolean = false
)

fun MainHomeTagData.toDomain(): MainHomeTag {
    return MainHomeTag(
        id = id,
        name = name,
        description = description,
        imageUrl = imageUrl,
        subscriberCount = subscriberCount,
        categoryId = categoryId,
        tagType = tagType,
        isSubscribed = isSubscribed
    )
}

fun MainHomeTag.toData(): MainHomeTagData {
    return MainHomeTagData(
        id = id,
        name = name,
        description = description,
        imageUrl = imageUrl,
        subscriberCount = subscriberCount,
        categoryId = categoryId,
        tagType = tagType,
        isSubscribed = isSubscribed
    )
}