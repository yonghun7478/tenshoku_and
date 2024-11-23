package com.example.tokitoki.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "my_tags",
    indices = [
        Index(value = ["userId"]),
        Index(value = ["tagId"]),
        Index(value = ["userId", "tagId"], unique = true) // 중복 방지
    ]
)
data class MyTagEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // 고유 ID
    val userId: Int = 0, // 사용자 ID
    val tagId: Int   // 태그 ID (다른 데이터베이스의 TagEntity 참조)
)