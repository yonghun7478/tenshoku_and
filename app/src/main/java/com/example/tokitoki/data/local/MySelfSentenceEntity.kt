package com.example.tokitoki.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "myself_sentence_item")
data class MySelfSentenceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String = "",
    val typeColor: String = "FF36C2CE",
    val sentence: String = ""
)