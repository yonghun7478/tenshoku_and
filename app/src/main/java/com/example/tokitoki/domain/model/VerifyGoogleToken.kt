package com.example.tokitoki.domain.model

data class VerifyGoogleToken(
    val id:String,
    val idToken:String,
    val accessToken:String,
    val refreshToken:String
)
