package com.example.tokitoki.ui.constants

object AboutMeNameConstants {
    const val TAG = "AboutMeNameScreen"
}

sealed class AboutMeNameAction {
    data object NOTHING : AboutMeNameAction()
    data object NEXT : AboutMeNameAction()
    data object EDIT_OK : AboutMeNameAction()
    data object PREVIOUS : AboutMeNameAction()
    data object DIALOG_OK : AboutMeNameAction()
}