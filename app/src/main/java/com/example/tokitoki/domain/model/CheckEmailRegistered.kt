package com.example.tokitoki.domain.model

data class CheckEmailRegistered(val isRegistered: Boolean, val accessToken: String, val refreshToken: String)
