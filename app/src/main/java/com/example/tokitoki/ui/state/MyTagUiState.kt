package com.example.tokitoki.ui.state

// UiState
data class MyTagUiState(
    val selectedTags: List<TagItemUiState> = listOf(), // 변경
    val searchQuery: String = "",
    val searchResults: List<TagItemUiState> = listOf(), // 변경
    val recentSearches: List<TagItemUiState> = listOf(), // 변경
    val trendingTags: List<TagItemUiState> = listOf(),
    val todayTags: TagItemUiState = TagItemUiState("", "", 0),
    val myTags: List<TagItemUiState> = listOf(),
    val suggestedTags: List<TagItemUiState> = listOf()
)

data class TagItemUiState( //임시
    val name: String,
    val imageUrl: String,
    val userCount: Int
)