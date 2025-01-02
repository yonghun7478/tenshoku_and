package com.example.tokitoki.ui.state

data class AboutMeGenderState(
    val selectedGender: Gender = Gender.NONE,
    val showDialog: Boolean = false,
)

enum class Gender {
    NONE, MALE, FEMALE
}