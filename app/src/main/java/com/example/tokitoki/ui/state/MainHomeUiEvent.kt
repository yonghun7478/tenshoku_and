package com.example.tokitoki.ui.state

sealed class MainHomeUiEvent {
    data class TabSelected(val tab: MainHomeTab) : MainHomeUiEvent()
}