package com.example.tokitoki.domain.model

import com.example.tokitoki.data.model.MainHomeTagData
import com.example.tokitoki.ui.state.MainHomeTagItemUiState

data class MainHomeTag(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val subscriberCount: Int
) {
    fun toPresentation(): MainHomeTagItemUiState = MainHomeTagItemUiState(
        id = this.id,
        name = this.name,
        description = this.description,
        imageUrl = this.imageUrl,
        subscriberCount = this.subscriberCount
    )

    fun toData(): MainHomeTagData = MainHomeTagData(
        id = this.id,
        name = this.name,
        description = this.description,
        imageUrl = this.imageUrl,
        subscriberCount = this.subscriberCount
    )
}