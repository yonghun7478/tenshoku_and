package com.example.tokitoki.data.model

data class LikedUserDto(
    val id: String,
    val nickname: String,
    val age: Int,
    val location: String,
    val profileImageUrl: String,
    val introduction: String?,
    val occupation: String?,
    val likedAt: Long // 좋아요를 누른 시간 (커서 값으로 사용)
) 