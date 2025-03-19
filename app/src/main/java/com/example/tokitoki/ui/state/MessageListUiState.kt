package com.example.tokitoki.ui.state

import com.example.tokitoki.domain.model.MatchingUser
import com.example.tokitoki.domain.model.PreviousChat

data class MessageListUiState(
    val matchingUsers: List<MatchingUser>,
    val previousChats: List<PreviousChat>,
    val isLoading: Boolean = false, // 추가: 로딩 상태 관리
    val errorMessage: String? = null // 추가: 에러 메시지 관리
)