package com.example.tokitoki.domain.converter

import com.example.tokitoki.data.model.TokensResponse
import com.example.tokitoki.domain.model.Tokens

object TokensConverter {
    fun fromResponse(response: TokensResponse): Tokens {
        return Tokens(response.token, response.refreshToken)
    }

    fun toResponse(tokens: Tokens): TokensResponse {
        return TokensResponse(tokens.token, tokens.refreshToken)
    }
}
