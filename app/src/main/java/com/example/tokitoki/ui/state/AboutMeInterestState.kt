package com.example.tokitoki.ui.state

import com.example.tokitoki.ui.model.InterestItemUiModel

data class AboutMeInterestState(
    val showDialog: Boolean = false,
    val hobbyList : List<InterestItemUiModel> = listOf(),
    val lifeStyleList : List<InterestItemUiModel> = listOf(),
    val valuesList : List<InterestItemUiModel> = listOf(),
    val categoryList: List<String> = listOf("趣味", "ライフスタイル", "価値観") // 카테고리 리스트 추가
)
