package com.example.tokitoki.ui.constants

object RegisterWithEmailConstants {
    const val TAG = "TAG_REGISTER_WITH_EMAIL"
}

sealed class RegisterWithEmailAction {
    data object Nothing : RegisterWithEmailAction()
    data object Submit : RegisterWithEmailAction()
}