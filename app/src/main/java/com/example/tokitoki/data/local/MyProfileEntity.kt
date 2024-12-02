package com.example.tokitoki.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_profile")
data class MyProfileEntity(
    @PrimaryKey val id: Int = 0,
    val name: String,
    val age: String,
    val isMale: Boolean,
    val thumbnailImageUri: String,
    val mySelfSentence: String
)
