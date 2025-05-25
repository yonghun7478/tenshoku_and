package com.example.tokitoki.domain.model

import com.example.tokitoki.data.model.MainHomeTagData
import com.example.tokitoki.ui.state.MainHomeTagItemUiState
import com.example.tokitoki.data.model.TagType

data class MainHomeTag(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val subscriberCount: Int,
    val categoryId: String,
    val tagType: TagType
) {
    fun toPresentation(): MainHomeTagItemUiState = MainHomeTagItemUiState(
        id = this.id,
        name = this.name,
        description = this.description,
        imageUrl = this.imageUrl,
        subscriberCount = this.subscriberCount,
        categoryId = this.categoryId,
        tagType = this.tagType
    )

    fun toData(): MainHomeTagData = MainHomeTagData(
        id = this.id,
        name = this.name,
        description = this.description,
        imageUrl = this.imageUrl,
        subscriberCount = this.subscriberCount,
        categoryId = this.categoryId,
        tagType = this.tagType
    )
}