package com.example.tokitoki.ui.state

import com.example.tokitoki.ui.model.TagTypeItem
import com.example.tokitoki.ui.model.TagItem

data class AboutMeTagState(
    val showDialog: Boolean = false,
    val tagsByTagType: Map<String, List<TagItem>> = emptyMap(),
    val tagTypeList: List<TagTypeItem> = listOf(),
    val isEditMode: Boolean = false
)
