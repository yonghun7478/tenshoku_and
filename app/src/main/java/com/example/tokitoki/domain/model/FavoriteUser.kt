package com.example.tokitoki.domain.model

import com.example.tokitoki.data.model.FavoriteUserDto

data class FavoriteUser(
    val id: String,
    val thumbnailUrl: String,
    val name: String,
    val age: Int,
    val location: String,
    val height: Int,
    val job: String,
    val hasRoommate: Boolean,
    val siblings: String,
    val bloodType: String,
    val timestamp: Long,
    var isSendingMiten: Boolean = false
)

fun FavoriteUser.toDto(): FavoriteUserDto {
    return FavoriteUserDto(
        id = this.id,
        thumbnail_url = this.thumbnailUrl,
        name = this.name,
        age = this.age,
        location = this.location,
        height = this.height,
        job = this.job,
        has_roommate = this.hasRoommate,
        siblings = this.siblings,
        blood_type = this.bloodType,
        timestamp = this.timestamp // 매핑 시 timestamp 처리
    )
}