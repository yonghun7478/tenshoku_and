package com.example.tokitoki.data.local

data class UserDetailEntity(
    val id: String,
    val name: String,
    val birthDay: String,
    val isMale: Boolean,
    val mySelfSentenceId: Int,
    val email: String,
    val thumbnailUrl: String
) 