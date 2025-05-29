package com.example.tokitoki.ui.state

import com.example.tokitoki.domain.model.UserDetail
import com.example.tokitoki.domain.model.MessageStatus

data class MessageDetailUiState(
    val userProfile: UserDetail? = null,
    val isLoading: Boolean = false,
    val isSending: Boolean = false,
    val error: String? = null,
    val messageStatus: MessageStatus? = null,
) 