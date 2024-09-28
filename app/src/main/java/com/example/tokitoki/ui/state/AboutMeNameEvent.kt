package com.example.tokitoki.ui.state

import com.example.tokitoki.ui.constants.AboutMeNameAction

sealed class AboutMeNameEvent {
    data object NOTHING : AboutMeNameEvent()
    data class ACTION(val action: AboutMeNameAction) : AboutMeNameEvent()
}