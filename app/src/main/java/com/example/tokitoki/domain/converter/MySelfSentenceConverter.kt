package com.example.tokitoki.domain.converter

import com.example.tokitoki.data.local.MySelfSentenceEntity
import com.example.tokitoki.domain.model.MySelfSentence

object MySelfSentenceConverter {
    fun entityToDomain(entity: MySelfSentenceEntity): MySelfSentence {
        return MySelfSentence(
            id = entity.id,
            type = entity.type,
            typeColor = entity.typeColor,
            sentence = entity.sentence
        )
    }

    fun domainToEntity(domain: MySelfSentence): MySelfSentenceEntity {
        return MySelfSentenceEntity(
            id = domain.id,
            type = domain.type,
            typeColor = domain.typeColor,
            sentence = domain.sentence
        )
    }
}
