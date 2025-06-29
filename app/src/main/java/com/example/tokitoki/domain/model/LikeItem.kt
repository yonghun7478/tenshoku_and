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
    val likedAt: Long,
    val personalityTrait: String?,
    val lifestyle: String,
    val datingPhilosophy: String?,
    val marriageView: String?
)

data class LikeResult(
    val likes: List<LikeItem>,
    val nextCursor: Long?
)