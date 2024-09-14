package com.example.tokitoki.ui.state

data class AboutMeGenderState(
    val selectedGender: Gender? = null
)

enum class Gender {
    MALE, FEMALE
}