package com.example.tokitoki.domain.model

data class MyProfile(
    val id: Int = 0, // 단일 사용자이므로 항상 0일 것으로 보임
    val name: String = "",
    val age: String = "",
    val isMale: Boolean = false,
    val mySelfSentence: String = ""
)
