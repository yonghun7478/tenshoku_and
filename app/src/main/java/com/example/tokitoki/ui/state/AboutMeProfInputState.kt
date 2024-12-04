package com.example.tokitoki.ui.state

import com.example.tokitoki.ui.model.MySelfSentenceItem

data class AboutMeProfInputState(
    val myselfSentenceList: List<MySelfSentenceItem> = listOf(),
)
