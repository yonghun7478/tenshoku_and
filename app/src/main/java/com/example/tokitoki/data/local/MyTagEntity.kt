package com.example.tokitoki.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "my_tags",
    indices = [Index(value = ["tagId"], unique = true)] // 중복 방지
)

data class MyTagEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tagId: Int // 태그 ID
)