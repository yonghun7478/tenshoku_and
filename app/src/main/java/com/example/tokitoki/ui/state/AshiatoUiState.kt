package com.example.tokitoki.ui.state

import com.example.tokitoki.domain.model.AshiatoTimeline

data class AshiatoUiState(
    val isLoadingInitial: Boolean = true, // 초기 로딩 중 상태 (isLoading으로 변경 예정)
    // isLoadingMore 제거됨
    val isRefreshing: Boolean = false,    // 새로고침 중 상태
    val timeline: AshiatoTimeline = AshiatoTimeline(emptyList()), // 화면에 표시될 아시아토 기록
    val error: Throwable? = null,         // 오류 상태
    val canLoadMore: Boolean = true       // 더 로드할 데이터가 있는지 여부
)