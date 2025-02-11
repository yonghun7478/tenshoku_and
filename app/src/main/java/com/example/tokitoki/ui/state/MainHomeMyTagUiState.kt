package com.example.tokitoki.ui.state

data class MainHomeMyTagUiState(
    val trendingTags: List<Pair<String, String>> = emptyList(),
    val selectedTags: List<Pair<String, String>> = emptyList(),
    val recommendedTags: List<Pair<String, String>> = emptyList()
)