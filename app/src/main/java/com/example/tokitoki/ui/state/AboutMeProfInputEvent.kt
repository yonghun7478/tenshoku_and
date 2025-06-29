package com.example.tokitoki.ui.state

import com.example.tokitoki.ui.constants.AboutMeProfInputAction

sealed class AboutMeProfInputEvent {
    data object NOTHING : AboutMeProfInputEvent()
    data class ACTION(val action: AboutMeProfInputAction) : AboutMeProfInputEvent()
}