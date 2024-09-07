package com.example.tokitoki.ui.constants

class EmailVerificationConstants {
}

sealed class EmailVerificationAction {
    data object Nothing : EmailVerificationAction()
    data object SUBMIT : EmailVerificationAction()
}