package com.example.tokitoki.ui.state

import com.example.tokitoki.ui.model.UserUiModel

data class MainHomeSearchUiState(
    val state: MainHomeSearchState = MainHomeSearchState.NOTHING,
    val orderType: OrderType = OrderType.LOGIN,
    val users: List<UserUiModel> = emptyList(), // 유저 리스트
    val isLastPage: Boolean = false // 마지막 페이지 여부
)

enum class OrderType {
    LOGIN,
    REGISTRATION
}

enum class MainHomeSearchState {
    NOTHING,
    LOADING,
    INITIALIZED
}