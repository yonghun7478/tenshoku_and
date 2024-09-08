package com.example.tokitoki.ui.state.backup

sealed class UserUiEvent {
    object NOTHING : UserUiEvent()
    object NextScreen : UserUiEvent()
}