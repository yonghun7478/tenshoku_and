package com.example.tokitoki.domain.model

data class Credential(
    val id: String = "",
    val token: String? = "",
    val type: CredentialType = CredentialType.UNKNOWN
)

enum class CredentialType {
    GOOGLE_ID_TOKEN,
    PASSWORD,
    PASSKEY,
    UNKNOWN
}