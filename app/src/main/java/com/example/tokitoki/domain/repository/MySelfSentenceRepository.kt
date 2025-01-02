package com.example.tokitoki.domain.repository

import com.example.tokitoki.domain.model.MySelfSentence

interface MySelfSentenceRepository {
    suspend fun getAllSentences(): List<MySelfSentence>

    suspend fun getSentence(id: Int): MySelfSentence
}