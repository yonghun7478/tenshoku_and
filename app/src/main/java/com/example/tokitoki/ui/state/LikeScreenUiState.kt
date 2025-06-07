package com.example.tokitoki.ui.state

// Data Classes (for UiState)
data class LikeScreenUiState(
    val receivedLikes: List<LikeItemUiState> = emptyList(),
    val showSnackBar: Boolean = false,
    val receivedLikesIsRefreshing: Boolean = false,
    val receivedCursor: Long? = null, // 추가: 받은 좋아요 커서
    val sentCursor: Long? = null,     // 추가: 보낸 좋아요 커서
    val matchedCursor: Long? = null    // 추가: 매칭된 좋아요 커서
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

data class DeleteItemState(
    val itemId: String? = null, // 삭제할 아이템의 ID (null이면 삭제할 아이템이 없음을 의미)
    val showDialog: Boolean = false // 대화상자 표시 여부
)

// Tab enum
enum class LikeTab(val title: String) {
    RECEIVED("いいねされた"),
    SENT("いいねした"),
}
