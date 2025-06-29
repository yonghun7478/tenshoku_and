package com.example.tokitoki.data.model

import com.example.tokitoki.domain.model.MainHomeTagSubscriber

data class MainHomeTagSubscriberData(
    val userId: String,
    val profileImageUrl: String,
    val age: Int,
    val location: String
)

fun MainHomeTagSubscriberData.toDomain(): MainHomeTagSubscriber {
    return MainHomeTagSubscriber(
        userId = userId,
        profileImageUrl = profileImageUrl,
        age = age,
        location = location
    )
} 