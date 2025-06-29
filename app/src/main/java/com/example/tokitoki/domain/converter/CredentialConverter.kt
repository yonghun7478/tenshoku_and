package com.example.tokitoki.domain.converter

import androidx.credentials.GetCredentialResponse
import com.example.tokitoki.domain.model.Credential

object CredentialConverter {
    fun dataToDomain(response: GetCredentialResponse): Credential {
        return Credential()
    }
}
