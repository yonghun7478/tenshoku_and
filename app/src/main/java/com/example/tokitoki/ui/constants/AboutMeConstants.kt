package com.example.tokitoki.ui.constants

class AboutMeConstants {
}

sealed class AboutMeAction {
    data object NOTHING : AboutMeAction()
    data object SUBMIT : AboutMeAction()
}