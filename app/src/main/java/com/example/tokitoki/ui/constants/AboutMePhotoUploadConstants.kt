package com.example.tokitoki.ui.constants

class AboutMePhotoUploadConstants {
}

sealed class AboutMePhotoUploadAction {
    data object NOTHING : AboutMePhotoUploadAction()
    data object SUBMIT : AboutMePhotoUploadAction()
}