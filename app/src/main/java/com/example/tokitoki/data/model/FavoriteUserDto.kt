package com.example.tokitoki.data.model

import com.example.tokitoki.domain.model.FavoriteUser

data class FavoriteUserDto(
    val id: String,
    val thumbnailUrl: String?,
    val name: String,
    val age: Int,
    val location: String,
    val occupation: String?,
    val bloodType: String?,
    val timestamp: Long,
    val isSendingMiten: Boolean,
    val personalityTrait: String?,
    val lifestyle: String?,
    val datingPhilosophy: String?,
    val marriageView: String?
) {
    fun toDomainModel(): FavoriteUser {
        return FavoriteUser(
            id = id,
            thumbnailUrl = thumbnailUrl,
            name = name,
            age = age,
            location = location,
            occupation = occupation,
            bloodType = bloodType,
            timestamp = timestamp,
            isSendingMiten = isSendingMiten,
            personalityTrait = personalityTrait,
            lifestyle = lifestyle ?: "", // 기본값 설정 (nullable 아님)
            datingPhilosophy = datingPhilosophy,
            marriageView = marriageView
        )
    }
}