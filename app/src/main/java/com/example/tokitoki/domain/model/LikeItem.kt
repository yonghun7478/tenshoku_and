package com.example.tokitoki.domain.model

data class LikeItem(
    val id: String,
    val thumbnail: String,
    val nickname: String,
    val age: Int,
    val introduction: String,
    val receivedTime: Long,
    val location: String,
    val occupation: String?,
    val likedAt: Long
)

data class LikeResult(
    val likes: List<LikeItem>,
    val nextCursor: Long?
)