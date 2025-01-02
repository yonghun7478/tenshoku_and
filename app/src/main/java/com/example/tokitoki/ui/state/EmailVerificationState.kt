package com.example.tokitoki.ui.state

data class EmailVerificationState(
    val code: String = "",
    val showDialog: Boolean = false,
)