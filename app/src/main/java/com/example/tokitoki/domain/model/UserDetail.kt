package com.example.tokitoki.domain.model

data class UserDetail(
    val id: String = "",
    val name: String = "",
    val birthDay: String = "",
    val isMale: Boolean = false,
    val mySelfSentenceId: Int = 0,
    val email: String = "",
    val thumbnailUrl: String = ""
) 