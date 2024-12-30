package com.example.tokitoki.domain.converter

import com.example.tokitoki.data.model.VerifyEmailResponse
import com.example.tokitoki.domain.model.VerifyEmail

object VerifyEmailConverter {
    fun fromResponse(response: VerifyEmailResponse): VerifyEmail {
        return VerifyEmail(response.token, response.refreshToken)
    }

    fun toResponse(verifyEmail: VerifyEmail): VerifyEmailResponse {
        return VerifyEmailResponse(verifyEmail.accessToken, verifyEmail.refreshToken)
    }
}
