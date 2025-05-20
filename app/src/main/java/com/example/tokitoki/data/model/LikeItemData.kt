package com.example.tokitoki.data.model

import com.example.tokitoki.domain.model.LikeItem

data class LikeItemData(
    val id: String,
    val thumbnail: String,
    val nickname: String,
    val age: Int,
    val introduction: String,
    val receivedTime: Long,
    val location: String,
    val occupation: String?,
    val likedAt: Long
) {
    // 도메인 모델로 변환
    fun toDomain(): LikeItem = LikeItem(
        id = id,
        thumbnail = thumbnail,
        nickname = nickname,
        age = age,
        introduction = introduction,
        receivedTime = receivedTime,
        location = location,
        occupation = occupation,
        likedAt = likedAt
    )
}