package com.example.tokitoki.ui.state

import com.example.tokitoki.ui.model.UserUiModel

data class MainHomeSearchUiState(
    val dataByLogin: MainHomeSearchUiStateData = MainHomeSearchUiStateData(),
    val dataByRegister: MainHomeSearchUiStateData = MainHomeSearchUiStateData(),
    val orderType: OrderType = OrderType.LOGIN
)

data class MainHomeSearchUiStateData(
    val state: MainHomeSearchState = MainHomeSearchState.NOTHING,
    val showShimmerEffect: Boolean = false,
    val users: List<UserUiModel> = emptyList(), // 유저 리스트
    val isLastPage: Boolean = false, // 마지막 페이지 여부
    val isRefreshing: Boolean = false,
    val cursor: String? = null
)

fun MainHomeSearchUiState.currentData(): MainHomeSearchUiStateData {
    return if (orderType == OrderType.LOGIN) dataByLogin else dataByRegister
}

fun MainHomeSearchUiState.updateData(newData: MainHomeSearchUiStateData): MainHomeSearchUiState {
    return if (orderType == OrderType.LOGIN) {
        copy(dataByLogin = newData)
    } else {
        copy(dataByRegister = newData)
    }
}

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