package com.example.tokitoki.ui.state

import com.example.tokitoki.domain.model.UserDetail
import com.example.tokitoki.domain.model.MainHomeTag

data class MessageDetailUiState(
    val isLoading: Boolean = false,
    val userProfile: UserDetail? = null,
    val error: String? = null,
    val isSending: Boolean = false,
    val userTags: List<MainHomeTag> = emptyList()
) 