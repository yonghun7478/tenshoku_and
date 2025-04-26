package com.example.tokitoki.data.mapper

import com.example.tokitoki.data.model.LikedUserDto
import com.example.tokitoki.domain.model.LikedUser

fun LikedUserDto.toDomain(): LikedUser {
    return LikedUser(
        id = id,
        nickname = nickname,
        age = age,
        location = location,
        profileImageUrl = profileImageUrl,
        introduction = introduction,
        occupation = occupation,
        likedAt = likedAt
    )
}

fun LikedUser.toDto(): LikedUserDto {
    return LikedUserDto(
        id = id,
        nickname = nickname,
        age = age,
        location = location,
        profileImageUrl = profileImageUrl,
        introduction = introduction,
        occupation = occupation,
        likedAt = likedAt
    )
} 