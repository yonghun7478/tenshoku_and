package com.example.tokitoki.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tags",
    foreignKeys = [ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["categoryId"])]
)
data class TagEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // 자동 증가
    val title: String,
    val url: String,
    val categoryId: Int
)