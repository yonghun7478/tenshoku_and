package com.example.tokitoki.ui.state

import com.example.tokitoki.ui.constants.AboutMeMyProfileAction

sealed class AboutMeMyProfileEvent {
    data object NOTHING : AboutMeMyProfileEvent()
    data class ACTION(val action: AboutMeMyProfileAction) : AboutMeMyProfileEvent()
}