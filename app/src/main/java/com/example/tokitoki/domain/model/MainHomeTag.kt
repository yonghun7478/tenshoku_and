package com.example.tokitoki.domain.model

import com.example.tokitoki.data.model.MainHomeTagData
import com.example.tokitoki.ui.state.MainHomeTagItemUiState

data class MainHomeTag(
    val name: String,
    val imageUrl: String,
    val userCount: Int
) {
    fun toPresentation(): MainHomeTagItemUiState = MainHomeTagItemUiState(
        name = this.name,
        imageUrl = this.imageUrl,
        userCount = this.userCount
    )

    fun toData(): MainHomeTagData = MainHomeTagData(
        name = this.name,
        imageUrl = this.imageUrl,
        userCount = this.userCount
    )
}