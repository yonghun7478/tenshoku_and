package com.example.tokitoki.ui.constants

class AboutMeMyProfileConstants {
}

sealed class AboutMeMyProfileAction {
    data object NOTHING : AboutMeMyProfileAction()
    data object SUBMIT : AboutMeMyProfileAction()
    data object FIX_PROFILE_INFO : AboutMeMyProfileAction()
    data object SUBETE_MIRU : AboutMeMyProfileAction()
}