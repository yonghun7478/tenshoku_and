package com.example.tokitoki.domain.model

data class LikeItem(
    val id: Int,
    val thumbnail: String,
    val nickname: String,
    val age: Int,
    val introduction: String,
    val receivedTime: Long // 좋아요 받은 시간 추가
)

data class LikeResult(
    val likes: List<LikeItem>,
    val nextCursor: Long?
)