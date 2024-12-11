package com.example.tokitoki.ui.converter

import com.example.tokitoki.domain.model.MyTag
import com.example.tokitoki.domain.model.Tag
import com.example.tokitoki.ui.model.TagItem

object TagUiConverter {
    fun domainToUi(tag: Tag): TagItem {
        return TagItem(
            id = tag.id,
            title = tag.title,
            url = tag.url
        )
    }

    fun uiToDomain(tagItem: TagItem): MyTag {
        return MyTag(tagId = tagItem.id)
    }
}