package com.example.tokitoki.ui.constants

object AboutMeGenderConstants {
    const val TAG = "AboutMeGenderScreen"
}

sealed class AboutMeGenderAction {
    data object NOTHING : AboutMeGenderAction()
    data object MALE_CLICK : AboutMeGenderAction()
    data object FEMALE_CLICK : AboutMeGenderAction()
    data object NEXT : AboutMeGenderAction()
    data object PREVIOUS : AboutMeGenderAction()
    data object DIALOG_OK : AboutMeGenderAction()
}