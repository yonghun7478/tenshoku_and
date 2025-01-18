package com.example.tokitoki.ui.state

sealed class MainUiEvent {
    data class SelectBottomItem(val item: MainBottomItem) : MainUiEvent()
}