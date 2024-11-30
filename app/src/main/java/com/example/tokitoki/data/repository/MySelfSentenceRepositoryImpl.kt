package com.example.tokitoki.data.repository

import com.example.tokitoki.data.local.MySelfSentenceDao
import com.example.tokitoki.domain.converter.MySelfSentenceConverter
import com.example.tokitoki.domain.model.MySelfSentence
import com.example.tokitoki.domain.repository.MySelfSentenceRepository
import javax.inject.Inject

class MySelfSentenceRepositoryImpl @Inject constructor(
    private val mySelfSentenceDao: MySelfSentenceDao,
) : MySelfSentenceRepository {
    override suspend fun getAllSentences(): List<MySelfSentence> {
        return mySelfSentenceDao.getAllSentences().map { MySelfSentenceConverter.entityToDomain(it) }
    }
}