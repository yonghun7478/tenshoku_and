package com.example.tokitoki.ui.state

import com.example.tokitoki.ui.model.UserUiModel

data class MainHomeSearchUiState(
    val dataByLogin: MainHomeSearchUiStateData = MainHomeSearchUiStateData(),
    val dataByRegister: MainHomeSearchUiStateData = MainHomeSearchUiStateData(),
    val orderType: OrderType = OrderType.LOGIN
)

data class MainHomeSearchUiStateData(
    val state: MainHomeSearchState = MainHomeSearchState.NOTHING,
    val users: List<UserUiModel> = emptyList(), // 유저 리스트
    val isLastPage: Boolean = false, // 마지막 페이지 여부
    val isRefreshing: Boolean = false
)

enum class OrderType {
    LOGIN,
    REGISTRATION
}

enum class MainHomeSearchState {
    NOTHING,
    ERROR,
    LOADING,
    COMPLETED,
}