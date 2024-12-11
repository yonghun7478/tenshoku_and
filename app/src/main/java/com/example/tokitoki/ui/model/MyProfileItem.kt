package com.example.tokitoki.ui.model

import android.net.Uri

data class MyProfileItem(
    val name: String = "",
    val age: String = "",
    val myTagItems: List<MyTagItem> = listOf(),
    val mySelfSentence: String = "",
)

data class MyTagItem(
    val title: String = "",
    val url: String = "",
    val categoryTitle: String = "",
)