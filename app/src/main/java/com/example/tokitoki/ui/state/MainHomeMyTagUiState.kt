package com.example.tokitoki.ui.state

import com.example.tokitoki.domain.model.MainHomeTag

// UiState
data class MainHomeMyTagUiState(
    val selectedTags: List<MainHomeTagItemUiState> = listOf(),
    val searchQuery: String = "",
    val searchResults: List<MainHomeTagItemUiState> = listOf(),
    val recentSearches: List<MainHomeTagItemUiState> = listOf(),
    val trendingTags: List<MainHomeTagItemUiState> = listOf(),
    val todayTags: MainHomeTagItemUiState = MainHomeTagItemUiState("", "", "", "", 0),
    val myTags: List<MainHomeTagItemUiState> = listOf(),
)

data class MainHomeTagItemUiState(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String,
    val subscriberCount: Int
) {
    fun toDomain(): MainHomeTag {
        return MainHomeTag(
            id = this.id,
            name = this.name,
            description = this.description,
            imageUrl = this.imageUrl,
            subscriberCount = this.subscriberCount
        )
    }
}

data class SuggestedTagsUiState(
    val tags: List<MainHomeTagItemUiState> = listOf(),
    val canLoadMore: Boolean = true,
    val isLoading: Boolean = false
)