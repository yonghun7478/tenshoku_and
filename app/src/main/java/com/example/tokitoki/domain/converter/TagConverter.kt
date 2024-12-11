package com.example.tokitoki.domain.converter

import com.example.tokitoki.data.local.TagWithCategoryEntity
import com.example.tokitoki.domain.model.Tag

object TagConverter {
    fun dataToDomain(tagWithCategoryEntity: TagWithCategoryEntity): Tag {
        return Tag(
            id = tagWithCategoryEntity.tagId,
            title = tagWithCategoryEntity.tagTitle,
            url = tagWithCategoryEntity.tagUrl,
            categoryId = tagWithCategoryEntity.categoryId,
            categoryTitle = tagWithCategoryEntity.categoryTitle
        )
    }

    fun domainToData(tag: Tag): TagWithCategoryEntity {
        return TagWithCategoryEntity(
            tagId = tag.id,
            tagTitle = tag.title,
            tagUrl = tag.url,
            categoryId = tag.categoryId,
            categoryTitle = tag.categoryTitle
        )
    }
}