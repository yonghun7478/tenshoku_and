package com.example.tokitoki.ui.state

import com.example.tokitoki.ui.model.TagTypeItem
import com.example.tokitoki.ui.model.TagItem

data class FavoriteTagUiState(
    val tagsByTagType: Map<String, List<TagItem>> = emptyMap(),
    val tagTypeList: List<TagTypeItem> = listOf()
)