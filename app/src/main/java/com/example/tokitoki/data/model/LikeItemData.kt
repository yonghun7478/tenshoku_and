package com.example.tokitoki.data.model

import com.example.tokitoki.domain.model.LikeItem

data class LikeItemData(
    val id: Int,
    val thumbnail: String,
    val nickname: String,
    val age: Int,
    val introduction: String,
    val receivedTime: Long // 좋아요 받은 시간 추가
) {
    // 도메인 모델로 변환
    fun toDomain(): LikeItem = LikeItem(
        id = id,
        thumbnail = thumbnail,
        nickname = nickname,
        age = age,
        introduction = introduction,
        receivedTime = receivedTime // 추가
    )
}