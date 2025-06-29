package com.example.tokitoki.data.model

import com.example.tokitoki.domain.model.MatchingUser

data class MatchingUserDto(
    val userId: String,
    val userName: String,
    val profileImageUrl: String,
    val matchedTimestamp: Long // 정렬 및 커서 생성을 위한 타임스탬프 (예시)
)

fun MatchingUserDto.toDomain(): MatchingUser {
    return MatchingUser(
        id = this.userId,
        name = this.userName,
        thumbnail = this.profileImageUrl
        // matchedAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(this.matchedTimestamp), ZoneId.systemDefault()) // 필요 시 변환
    )
}
