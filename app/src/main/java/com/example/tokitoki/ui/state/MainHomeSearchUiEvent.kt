package com.example.tokitoki.ui.state

import com.example.tokitoki.common.ResultWrapper

sealed class MainHomeSearchUiEvent {
    data class UserSelected(val index: Int) : MainHomeSearchUiEvent()
    data class OrderSelected(val orderType: OrderType) : MainHomeSearchUiEvent()
    object OnRefreshing : MainHomeSearchUiEvent()
    object LoadMore : MainHomeSearchUiEvent() // 무한 스크롤
    data class Error(val errorType: ResultWrapper.ErrorType) : MainHomeSearchUiEvent() // 에러 처리
}