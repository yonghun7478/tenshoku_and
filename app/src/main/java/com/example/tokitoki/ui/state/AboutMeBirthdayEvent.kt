package com.example.tokitoki.ui.state

import com.example.tokitoki.ui.constants.AboutMeBirthdayAction

sealed class AboutMeBirthdayEvent {
    data object NOTHING : AboutMeBirthdayEvent()
    data class ACTION(val action: AboutMeBirthdayAction) : AboutMeBirthdayEvent()
}