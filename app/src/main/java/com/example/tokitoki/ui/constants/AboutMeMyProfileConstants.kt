package com.example.tokitoki.ui.constants

class AboutMeMyProfileConstants {
}

sealed class AboutMeMyProfileAction {
    data object NOTHING : AboutMeMyProfileAction()
    data object SUBMIT : AboutMeMyProfileAction()
    data object FIX_PICTURE : AboutMeMyProfileAction()
    data object FIX_NAME : AboutMeMyProfileAction()
    data object FIX_BIRTHDAY : AboutMeMyProfileAction()
    data object FIX_MY_TAG : AboutMeMyProfileAction()
    data object FIX_MY_SELF_SENTENCE : AboutMeMyProfileAction()
    data object CHECK_EVERYTHING : AboutMeMyProfileAction()
}