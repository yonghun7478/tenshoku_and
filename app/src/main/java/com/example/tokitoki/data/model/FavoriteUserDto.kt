package com.example.tokitoki.data.model

data class FavoriteUserDto(
    val thumbnail_url: String?, // 섬네일 URL (nullable)
    val name: String?,         // 이름 (nullable)
    val age: Int?,            // 나이 (nullable)
    val location: String?,      // 위치 (nullable)
    val height: Int?,          // 신장 (nullable)
    val job: String?,           // 직업 (nullable)
    val has_roommate: Boolean?, // 동거인 여부 (nullable)
    val siblings: String?,      // 형제자매 (nullable)
    val blood_type: String?     // 혈액형 (nullable)
)