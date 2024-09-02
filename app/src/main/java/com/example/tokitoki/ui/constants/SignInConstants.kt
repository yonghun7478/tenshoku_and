package com.example.tokitoki.ui.constants

object SignInConstants {
    const val TAG = "TAG_SIGN_IN"
}

sealed class SignInAction {
    data object Nothing : SignInAction()
    data object Help : SignInAction()
    data object Service : SignInAction()
    data object Privacy : SignInAction()
    data object Cookie : SignInAction()

    data object LoginWithEmail : SignInAction()

    data object LoginWithGoogle : SignInAction()
}