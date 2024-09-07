package com.example.tokitoki.ui.constants

sealed class EmailVerificationAction {
    data object Nothing : EmailVerificationAction()
    data object SUBMIT : EmailVerificationAction()
}