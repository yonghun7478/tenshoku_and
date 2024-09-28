package com.example.tokitoki.ui.state

import com.example.tokitoki.ui.constants.AboutMeSecondAction

sealed class AboutMeSecondEvent {
    data object NOTHING : AboutMeSecondEvent()
    data class ACTION(val action: AboutMeSecondAction) : AboutMeSecondEvent()
}