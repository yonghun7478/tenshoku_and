package com.example.tokitoki.ui.state

// UiState
data class MyTagUiState(
    val selectedTags: List<String> = listOf(),
    val searchQuery: String = "",
    val searchResults: List<String> = listOf(), // 임시
    val recentSearches: List<String> = listOf(), // 임시
    val trendingTags: List<TagItemUiState> = listOf(), // List<String> -> List<TagItemUiState>
    val todayTags : TagItemUiState = TagItemUiState("","",0),
    val myTags: List<TagItemUiState> = listOf(), // 임시
    val suggestedTags: List<TagItemUiState> = listOf() // 임시

)
data class TagItemUiState( //임시
    val name: String,
    val imageUrl: String,
    val userCount: Int
)