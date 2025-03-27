package com.example.tokitoki.ui.state

import com.example.tokitoki.domain.model.MatchingUser
import com.example.tokitoki.domain.model.PreviousChat

data class MessageListUiState(
    val matchingUsers: List<MatchingUser> = emptyList(),
    val previousChats: List<PreviousChat> = emptyList(),
    val isLoading: Boolean = false, // 전체 초기 로딩 상태
    val errorMessage: String? = null
    // 필요 시 섹션별 로딩 상태 추가
    // val isMatchingSectionLoading: Boolean = false,
    // val isPreviousChatSectionLoading: Boolean = false,
)
