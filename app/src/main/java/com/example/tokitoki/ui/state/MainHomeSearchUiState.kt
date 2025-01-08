package com.example.tokitoki.ui.state

data class MainHomeSearchUiState(
    val state: MainHomeSearchState = MainHomeSearchState.NOTHING,
    val orderType: OrderType = OrderType.LOGIN
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