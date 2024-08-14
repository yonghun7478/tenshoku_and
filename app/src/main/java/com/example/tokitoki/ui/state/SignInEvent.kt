package com.example.tokitoki.ui.state

import com.example.tokitoki.ui.util.SignInAction

sealed class SignInEvent {
    data object NOTHING : SignInEvent()
    data class ACTION(val action: SignInAction) : SignInEvent()
}