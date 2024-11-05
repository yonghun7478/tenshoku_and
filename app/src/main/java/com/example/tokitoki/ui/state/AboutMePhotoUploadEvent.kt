package com.example.tokitoki.ui.state

import com.example.tokitoki.ui.constants.AboutMePhotoUploadAction


sealed class AboutMePhotoUploadEvent {
    data object NOTHING : AboutMePhotoUploadEvent()
    data class ACTION(val action: AboutMePhotoUploadAction) : AboutMePhotoUploadEvent()
}