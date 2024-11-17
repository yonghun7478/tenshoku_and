package com.example.tokitoki.domain.model

import android.net.Uri

data class MyProfile(
    val name: String = "",
    val age: String = "",
    val thumbnailImageUri: Uri = Uri.EMPTY,
    val myTagItems: List<MyTag> = listOf(),
    val mySelfSentence: String = "",
)

data class MyTag(
    val title: String = "",
    val url: String = "",
    val categoryTitle: String = "",
)