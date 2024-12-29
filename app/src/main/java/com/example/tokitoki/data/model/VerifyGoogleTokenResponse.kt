package com.example.tokitoki.data.model

data class VerifyGoogleTokenResponse(
    val id:String,
    val idToken:String,
    val accessToken:String,
    val refreshToken:String
)