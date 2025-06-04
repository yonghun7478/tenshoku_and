package com.example.tokitoki.domain.model

data class UserDetail(
    val id: String = "",
    val name: String = "",
    val birthDay: String = "",
    val email: String = "",
    val thumbnailUrl: String = "",
    val isMale: Boolean = false,
    val age: Int = 0,
    val location: String = "",
    val myTags: List<String> = emptyList(),
    val introduction: String = "",
    val bloodType: String = "",
    val education: String = "",
    val occupation: String = "",
    val appearance: String = "",
    val datingPhilosophy: String = "",
    val marriageView: String = "",
    val personalityTraits: List<String> = emptyList(),
    val hobbies: List<String> = emptyList(),
    val lifestyle: String = ""
) 