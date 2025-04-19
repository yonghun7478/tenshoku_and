package com.example.tokitoki.domain.model

data class FavoriteUser(
    val thumbnailUrl: String,
    val name: String,
    val age: Int,
    val location: String,
    val height: Int,
    val job: String,
    val hasRoommate: Boolean,
    val siblings: String,
    val bloodType: String
)