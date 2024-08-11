package com.example.tokitoki.ui.state

sealed class UserUiEvent {
    object NOTHING : UserUiEvent()
    object NextScreen : UserUiEvent()
}