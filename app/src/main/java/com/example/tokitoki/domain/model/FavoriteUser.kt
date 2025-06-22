package com.example.tokitoki.domain.model

import com.example.tokitoki.data.model.FavoriteUserDto

data class FavoriteUser(
    val id: String,
    val thumbnailUrl: String,
    val name: String,
    val age: Int,
    val location: String,
    val job: String,
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
        job = this.job,
        blood_type = this.bloodType,
        timestamp = this.timestamp // 매핑 시 timestamp 처리
    )
}