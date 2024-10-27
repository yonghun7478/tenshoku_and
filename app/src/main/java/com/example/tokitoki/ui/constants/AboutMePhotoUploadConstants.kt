package com.example.tokitoki.ui.constants

class AboutMePhotoUploadConstants {
}

sealed class AboutMePhotoUploadAction {
    data object NOTHING : AboutMePhotoUploadAction()
    data object CLICK_INPUT_BOX : AboutMePhotoUploadAction()
    data object SUBMIT : AboutMePhotoUploadAction()
    data object DISSMISS_BOTTIOM_DIALOG : AboutMePhotoUploadAction()
    data object CLICK_LIBRARY : AboutMePhotoUploadAction()
    data object CLICK_TAKE_PICTURE : AboutMePhotoUploadAction()
}