package com.example.tokitoki.domain.model

data class LikedUser(
    val id: String,
    val nickname: String,
    val age: Int,
    val location: String,
    val profileImageUrl: String,
    val introduction: String?,
    val occupation: String?,
    val likedAt: Long
) 