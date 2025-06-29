package com.example.tokitoki.ui.state

import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.model.User

// 태그 상세화면 UI 상태

data class TagDetailUiState(
    val isLoading: Boolean = false,
    val tag: MainHomeTag? = null,
    val isSubscribed: Boolean = false,
    val subscribers: List<User> = emptyList(),
    val nextCursor: String? = null,
    val isLastPage: Boolean = false,
    val error: String? = null
) 