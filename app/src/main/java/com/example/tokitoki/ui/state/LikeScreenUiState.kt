package com.example.tokitoki.ui.state

// Data Classes (for UiState)
data class LikeScreenUiState(
    val receivedLikes: List<LikeItemUiState> = emptyList(),
    val receivedLikesIsRefreshing: Boolean = false,
    val receivedCursor: Long? = null, // 추가: 받은 좋아요 커서
)

data class LikeItemUiState(
    val id: String,
    val thumbnail: String, // Image resource ID (Int) or URL (String)
    val nickname: String,
    val age: Int,
    val introduction: String,
    val receivedTime: Long, // 추가
    val isRefreshing: Boolean = false // 새로고침 상태 추가
)

// Tab enum
enum class LikeTab(val title: String) {
    RECEIVED("いいねされた"),
    SENT("いいねした"),
}
