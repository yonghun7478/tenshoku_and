package com.example.tokitoki.ui.converter

import com.example.tokitoki.domain.model.TagType
import com.example.tokitoki.ui.model.TagTypeItem

object TagTypeUiConverter {
    fun tagTypeToUi(tagType: TagType): TagTypeItem {
        return TagTypeItem(
            id = tagType.ordinal, // TagType의 순서 값을 ID로 사용
            title = tagType.value  // TagType의 문자열 값을 title로 사용
        )
    }
}