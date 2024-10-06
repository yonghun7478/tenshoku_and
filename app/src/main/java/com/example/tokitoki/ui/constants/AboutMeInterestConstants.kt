package com.example.tokitoki.ui.constants

object AboutMeInterestConstants {
    const val TAG = "AboutMeInterestScreen"
}

sealed class AboutMeInterestAction {
    data object NOTHING : AboutMeInterestAction()
    data object NEXT : AboutMeInterestAction()
    data object PREVIOUS : AboutMeInterestAction()
    data object DIALOG_OK : AboutMeInterestAction()
    data class SelectedTab(val index: Int) : AboutMeInterestAction()
}