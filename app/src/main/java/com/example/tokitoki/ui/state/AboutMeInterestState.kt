package com.example.tokitoki.ui.state

import com.example.tokitoki.ui.model.InterestItemUiModel

data class AboutMeInterestState(
    val showDialog: Boolean = false,
    val hobbyList : List<InterestItemUiModel> = listOf(),
    val lifeStyleList : List<InterestItemUiModel> = listOf(),
    val valuesList : List<InterestItemUiModel> = listOf()
)
