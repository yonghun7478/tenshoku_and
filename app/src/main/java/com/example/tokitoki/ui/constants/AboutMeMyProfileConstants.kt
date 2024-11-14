package com.example.tokitoki.ui.constants

class AboutMeMyProfileConstants {
}

sealed class AboutMeMyProfileAction {
    data object NOTHING : AboutMeMyProfileAction()
    data object SUBMIT : AboutMeMyProfileAction()
}