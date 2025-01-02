package com.example.tokitoki.ui.state

import com.example.tokitoki.ui.constants.AboutMeGenderAction

sealed class AboutMeGenderEvent {
    data object NOTHING : AboutMeGenderEvent()
    data class ACTION(val action: AboutMeGenderAction) : AboutMeGenderEvent()
}