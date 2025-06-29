package com.example.tokitoki.ui.state

import com.example.tokitoki.ui.constants.AboutMeTagAction

sealed class AboutMeTagEvent {
    data object NOTHING : AboutMeTagEvent()
    data class ACTION(val action: AboutMeTagAction) : AboutMeTagEvent()
}