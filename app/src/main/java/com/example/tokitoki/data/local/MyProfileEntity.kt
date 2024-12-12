package com.example.tokitoki.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_profile")
data class MyProfileEntity(
    @PrimaryKey val id: Int = 0,
    val name: String,
    val birthDay: String,
    val isMale: Boolean,
    val mySelfSentence: String
)
