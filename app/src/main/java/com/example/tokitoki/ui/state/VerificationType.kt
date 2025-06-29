package com.example.tokitoki.ui.state

sealed class VerificationType {
    object Error : VerificationType()
    object GotoMainScreen : VerificationType()
    object GotoAboutMeScreen : VerificationType()
}