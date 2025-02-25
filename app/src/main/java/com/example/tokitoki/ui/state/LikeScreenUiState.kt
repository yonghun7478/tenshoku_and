package com.example.tokitoki.ui.state

// Data Classes (for UiState)
data class LikeScreenUiState(
    val selectedTab: LikeTab = LikeTab.RECEIVED,
    val isDeleteMode: Boolean = false,
    val receivedLikes: List<LikeItemUiState> = emptyList(),
    val sentLikes: List<LikeItemUiState> = emptyList(),
    val matchedLikes: List<LikeItemUiState> = emptyList(),
    val selectedItems: Set<Int> = emptySet(), // 선택된 아이템의 ID 저장 (삭제 모드용)
    val showDeleteDialog: Boolean = false,
    val showSnackBar: Boolean = false,
    val receivedLikesIsRefreshing: Boolean = false,
    val sentLikesIsRefreshing: Boolean = false,
    val matchedLikesIsRefreshing: Boolean = false
)

data class LikeItemUiState(
    val id: Int,
    val thumbnail: String, // Image resource ID (Int) or URL (String)
    val nickname: String,
    val age: Int,
    val introduction: String,
    val isChecked: Boolean = false, // 삭제 모드에서 체크박스 상태
    val isRefreshing: Boolean = false // 새로고침 상태 추가
)

// Tab enum
enum class LikeTab(val title: String) {
    RECEIVED("いいねされた"),
    SENT("いいねした"),
    MATCHED("両想い")
}
