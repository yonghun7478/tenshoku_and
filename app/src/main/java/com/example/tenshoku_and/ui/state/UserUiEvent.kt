package com.example.tenshoku_and.ui.state

sealed class UserUiEvent {
    object NOTHING : UserUiEvent()
    object NextScreen : UserUiEvent()
}