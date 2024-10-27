package com.example.tokitoki.ui.state

import android.net.Uri

data class AboutMePhotoUploadState(
    val showDialog: Boolean = false,
    val showBottomDialog: Boolean = false,
    val capturedImageUri: Uri = Uri.EMPTY
)
