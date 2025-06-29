package com.example.tokitoki.ui.converter

import com.example.tokitoki.domain.model.MyTag
import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.ui.model.TagItem

object TagUiConverter {
    fun domainToUi(tag: MainHomeTag): TagItem {
        return TagItem(
            id = tag.id.toIntOrNull() ?: 0,
            tagTypeId = tag.categoryId.toIntOrNull() ?: 0,
            title = tag.name,
            url = tag.imageUrl,
            desc = tag.description,
        )
    }

    fun uiToDomain(tagItem: TagItem): MyTag {
        return MyTag(tagId = tagItem.id, tagTypeId = tagItem.tagTypeId)
    }
}