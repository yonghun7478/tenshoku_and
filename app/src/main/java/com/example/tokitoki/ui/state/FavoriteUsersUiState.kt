package com.example.tokitoki.ui.state

import com.example.tokitoki.domain.model.FavoriteUser

data class FavoriteUsersUiState(
    val favoriteUsers: List<FavoriteUser> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasMore: Boolean = true // 다음 페이지가 있는지 여부
)
