package com.example.tokitoki.ui.state

import androidx.compose.runtime.Immutable

@Immutable
data class LikesAndAshiatoUiState(
    val selectedTabIndex: Int = 0,
    val likeState: LikeScreenUiState = LikeScreenUiState(),
    val ashiatoState: AshiatoUiState = AshiatoUiState()
)

enum class LikesAndAshiatoTab(val title: String) {
    LIKES("いいね"),
    ASHIATO("足あと")
} 