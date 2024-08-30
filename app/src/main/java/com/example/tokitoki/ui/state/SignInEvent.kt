package com.example.tokitoki.ui.state

import com.example.tokitoki.ui.constants.SignInAction

sealed class SignInEvent {
    data object NOTHING : SignInEvent()
    data class ACTION(val action: SignInAction) : SignInEvent()
}