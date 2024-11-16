package com.example.tokitoki.ui.state

import com.example.tokitoki.ui.model.MyProfileItem

data class AboutMeMyProfileState(
    val isInitialized: Boolean = false,
    val myProfileItem: MyProfileItem = MyProfileItem()
)
