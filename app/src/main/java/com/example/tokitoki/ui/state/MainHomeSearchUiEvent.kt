package com.example.tokitoki.ui.state

sealed class MainHomeSearchUiEvent {
    data class UserSelected(val index: Int) : MainHomeSearchUiEvent()
    data class OrderSelected(val orderType: OrderType) : MainHomeSearchUiEvent()
}