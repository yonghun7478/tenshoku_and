package com.example.tokitoki.ui.constants

object AboutMeBirthdayConstants {
    const val TAG = "AboutMeBirthdayScreen"
}

sealed class AboutMeBirthdayAction {
    data object NOTHING : AboutMeBirthdayAction()
    data object NEXT : AboutMeBirthdayAction()
    data object PREVIOUS : AboutMeBirthdayAction()
    data object DIALOG_OK : AboutMeBirthdayAction()
}