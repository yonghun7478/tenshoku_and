package com.example.tokitoki.ui.constants

class EmailVerificationConstants {
}

sealed class EmailVerificationAction {
    data object NOTHING : EmailVerificationAction()
    data object SUBMIT : EmailVerificationAction()
    data object SUCCESS : EmailVerificationAction()
}