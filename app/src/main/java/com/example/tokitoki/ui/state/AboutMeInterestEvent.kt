package com.example.tokitoki.ui.state

import com.example.tokitoki.ui.constants.AboutMeInterestAction

sealed class AboutMeInterestEvent {
    data object NOTHING : AboutMeInterestEvent()
    data class ACTION(val action: AboutMeInterestAction) : AboutMeInterestEvent()
}