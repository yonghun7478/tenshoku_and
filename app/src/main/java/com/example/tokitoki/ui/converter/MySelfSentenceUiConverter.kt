package com.example.tokitoki.ui.converter

import com.example.tokitoki.domain.model.MySelfSentence
import com.example.tokitoki.ui.model.MySelfSentenceItem

object MySelfSentenceUiConverter {
    fun domainToUi(mySelfSentence: MySelfSentence): MySelfSentenceItem {
        return MySelfSentenceItem(
            id = mySelfSentence.id,
            type = mySelfSentence.type,
            typeColor = mySelfSentence.typeColor,
            sentence = mySelfSentence.sentence,
        )
    }

    fun uiToDomain(mySelfSentenceItem: MySelfSentenceItem): MySelfSentence {
        return MySelfSentence(
            id = mySelfSentenceItem.id,
            type = mySelfSentenceItem.type,
            typeColor = mySelfSentenceItem.typeColor,
            sentence = mySelfSentenceItem.sentence,
        )
    }
}