package com.example.tokitoki.ui.constants

object AboutMeProfInputConstants {
    const val TAG = "AboutMeProfInputScreen"
}

sealed class AboutMeProfInputAction {
    data object NOTHING : AboutMeProfInputAction()
    data object NEXT : AboutMeProfInputAction()
    data object PREVIOUS : AboutMeProfInputAction()
    data object DIALOG_OK : AboutMeProfInputAction()
    data object SUBMIT : AboutMeProfInputAction()
}