package com.example.tokitoki.ui.state

// Data Classes (for UiState)
data class LikeScreenUiState(
    val selectedTab: LikeTab = LikeTab.RECEIVED,
    val receivedLikes: List<LikeItemUiState> = emptyList(),
    val sentLikes: List<LikeItemUiState> = emptyList(),
    val matchedLikes: List<LikeItemUiState> = emptyList(),
    val showSnackBar: Boolean = false,
    val receivedLikesIsRefreshing: Boolean = false,
    val sentLikesIsRefreshing: Boolean = false,
    val matchedLikesIsRefreshing: Boolean = false,
    val deleteItemState: DeleteItemState = DeleteItemState(),
    val deleteModeState: DeleteModeState = DeleteModeState(),
    val receivedCursor: Long? = null, // 추가: 받은 좋아요 커서
    val sentCursor: Long? = null,     // 추가: 보낸 좋아요 커서
    val matchedCursor: Long? = null    // 추가: 매칭된 좋아요 커서
)

data class LikeItemUiState(
    val id: Int,
    val thumbnail: String, // Image resource ID (Int) or URL (String)
    val nickname: String,
    val age: Int,
    val introduction: String,
    val receivedTime: Long, // 추가
    val isChecked: Boolean = false, // 삭제 모드에서 체크박스 상태
    val isRefreshing: Boolean = false // 새로고침 상태 추가
)

data class DeleteModeState(
    val isDeleteMode: Boolean = false,
    val selectedItems: Set<Int> = emptySet(),
    val showDialog: Boolean = false // 대화상자 표시 여부
)

data class DeleteItemState(
    val itemId: Int? = null, // 삭제할 아이템의 ID (null이면 삭제할 아이템이 없음을 의미)
    val showDialog: Boolean = false // 대화상자 표시 여부
)

// Tab enum
enum class LikeTab(val title: String) {
    RECEIVED("いいねされた"),
    SENT("いいねした"),
    MATCHED("両想い")
}
