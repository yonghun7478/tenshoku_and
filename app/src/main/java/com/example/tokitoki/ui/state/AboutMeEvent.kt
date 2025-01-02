package com.example.tokitoki.ui.state

import com.example.tokitoki.ui.constants.AboutMeAction

sealed class AboutMeEvent {
    data object NOTHING : AboutMeEvent()
    data class ACTION(val action: AboutMeAction) : AboutMeEvent()
}