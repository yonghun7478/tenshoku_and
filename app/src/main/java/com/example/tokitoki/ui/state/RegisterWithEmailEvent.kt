package com.example.tokitoki.ui.state

import com.example.tokitoki.ui.constants.RegisterWithEmailAction

sealed class RegisterWithEmailEvent {
    data object NOTHING : RegisterWithEmailEvent()
    data class ACTION(val action: RegisterWithEmailAction) : RegisterWithEmailEvent()
}