package com.example.tokitoki.ui.state

import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.model.UserDetail

data class UserDetailUiState(
    val userDetail: UserDetail,
    val userTags: List<MainHomeTag> = emptyList(),
    val isLiked: Boolean = false,
    val isFavorite: Boolean = false,
) 