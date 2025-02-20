package com.example.tokitoki.ui.state

// UiState
data class MainHomeMyTagUiState(
    val selectedTags: List<MainHomeTagItemUiState> = listOf(), // 변경
    val searchQuery: String = "",
    val searchResults: List<MainHomeTagItemUiState> = listOf(), // 변경
    val recentSearches: List<MainHomeTagItemUiState> = listOf(), // 변경
    val trendingTags: List<MainHomeTagItemUiState> = listOf(),
    val todayTags: MainHomeTagItemUiState = MainHomeTagItemUiState("", "", 0),
    val myTags: List<MainHomeTagItemUiState> = listOf(),
    val suggestedTags: List<MainHomeTagItemUiState> = listOf()
)

data class MainHomeTagItemUiState( //임시
    val name: String,
    val imageUrl: String,
    val userCount: Int
)