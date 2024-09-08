package com.example.tokitoki.ui.state

import com.example.tokitoki.ui.constants.EmailVerificationAction

sealed class EmailVerificationEvent {
    data object NOTHING : EmailVerificationEvent()
    data class ACTION(val action: EmailVerificationAction) : EmailVerificationEvent()
}