package com.example.tokitoki.ui.state

import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.model.TagType

// UiState
data class MainHomeMyTagUiState(
    val todayTag: MainHomeTagItemUiState? = null,
    val trendingTags: List<MainHomeTagItemUiState> = listOf(),
    val myTags: List<MainHomeTagItemUiState> = listOf(),
    val isLoadingTodayAndTrending: Boolean = false,
    val isLoadingMyTags: Boolean = false,
    val isLoadingSuggestedTags: Boolean = false,
    val snackbarMessage: String? = null
)

data class MainHomeTagItemUiState(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val subscriberCount: Int,
    val categoryId: String,
    val tagType: TagType,
    val isSubscribed: Boolean = false
) {
    fun toDomain(): MainHomeTag {
        return MainHomeTag(
            id = this.id,
            name = this.name,
            description = this.description,
            imageUrl = this.imageUrl,
            subscriberCount = this.subscriberCount,
            categoryId = this.categoryId,
            tagType = this.tagType,
            isSubscribed = this.isSubscribed
        )
    }
}

data class SuggestedTagsUiState(
    val tags: List<MainHomeTagItemUiState> = listOf(),
    val canLoadMore: Boolean = true,
)