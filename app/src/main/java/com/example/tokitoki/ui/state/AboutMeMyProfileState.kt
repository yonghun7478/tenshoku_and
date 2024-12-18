package com.example.tokitoki.ui.state

import android.net.Uri
import com.example.tokitoki.ui.model.MyProfileItem

data class AboutMeMyProfileState(
    val myProfileItem: MyProfileItem = MyProfileItem(),
    val uri: Uri = Uri.EMPTY
)
