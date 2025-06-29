package com.example.tokitoki.ui.constants

object AboutMeTagConstants {
    const val TAG = "AboutMeTagScreen"
}

sealed class AboutMeTagAction {
    data object NOTHING : AboutMeTagAction()
    data object NEXT : AboutMeTagAction()
    data object PREVIOUS : AboutMeTagAction()
    data object DIALOG_OK : AboutMeTagAction()
    data object EDIT_OK : AboutMeTagAction()
    data class SelectedTab(val index: Int) : AboutMeTagAction()
    data class ITEM_CLICKED(val tagType:String, val index: Int) : AboutMeTagAction()
}