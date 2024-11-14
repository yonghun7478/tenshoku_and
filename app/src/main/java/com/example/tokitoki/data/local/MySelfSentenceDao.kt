package com.example.tokitoki.data.local

import androidx.room.Dao
import androidx.room.Query

@Dao
interface MySelfSentenceDao {
    @Query("SELECT * FROM myself_sentence_item")
    suspend fun getAllSentences(): List<MySelfSentenceEntity>
}