package com.example.tokitoki.ui.state

import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.model.TagType

/**
 * 마이태그 리스트 화면의 UI 상태
 */
data class MyTagListUiState(
    val selectedTab: TagType = TagType.HOBBY,
    val tagLists: Map<TagType, List<MainHomeTag>> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null
) 