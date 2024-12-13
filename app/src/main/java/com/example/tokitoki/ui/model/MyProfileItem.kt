package com.example.tokitoki.ui.model


data class MyProfileItem(
    val name: String = "",
    val age: String = "",
    val myTagItems: List<MyTagItem> = listOf(),
    val mySelfSentence: String = "",
)