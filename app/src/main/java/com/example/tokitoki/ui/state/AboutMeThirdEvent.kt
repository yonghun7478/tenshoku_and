package com.example.tokitoki.ui.state

import com.example.tokitoki.ui.constants.AboutMeThirdAction

sealed class AboutMeThirdEvent {
    data object NOTHING : AboutMeThirdEvent()
    data class ACTION(val action: AboutMeThirdAction) : AboutMeThirdEvent()
}