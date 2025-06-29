package com.example.tokitoki.domain.model

import com.example.tokitoki.data.model.FavoriteUserDto

data class FavoriteUser(
    val id: String,
    val thumbnailUrl: String?,
    val name: String,
    val age: Int,
    val location: String,
    val occupation: String?,
    val bloodType: String?,
    val timestamp: Long,
    val isSendingMiten: Boolean = false,
    val personalityTrait: String?,
    val lifestyle: String,
    val datingPhilosophy: String?,
    val marriageView: String?
) {
    fun toDto(): FavoriteUserDto {
        return FavoriteUserDto(
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
            lifestyle = lifestyle,
            datingPhilosophy = datingPhilosophy,
            marriageView = marriageView
        )
    }
}