package com.example.tokitoki.data.model

import com.example.tokitoki.domain.model.FavoriteUser

data class FavoriteUserDto(
    val thumbnail_url: String?,
    val name: String?,
    val age: Int?,
    val location: String?,
    val height: Int?,
    val job: String?,
    val has_roommate: Boolean?,
    val siblings: String?,
    val blood_type: String?,
    val timestamp: Long? // 추가된 timestamp 필드
)

fun FavoriteUserDto.toDomainModel(): FavoriteUser {
    return FavoriteUser(
        thumbnailUrl = this.thumbnail_url ?: "",
        name = this.name ?: "",
        age = this.age ?: 0,
        location = this.location ?: "",
        height = this.height ?: 0,
        job = this.job ?: "",
        hasRoommate = this.has_roommate ?: false,
        siblings = this.siblings ?: "",
        bloodType = this.blood_type ?: "",
        timestamp = this.timestamp ?: 0L // 매핑 시 timestamp 처리
    )
}