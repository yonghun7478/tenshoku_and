package com.example.tokitoki.domain.repository

import com.example.tokitoki.domain.model.LikeItem

interface LikeRepository {
    suspend fun getReceivedLikes(): Result<List<LikeItem>>
    suspend fun getSentLikes(): Result<List<LikeItem>>
    suspend fun getMatchedLikes(): Result<List<LikeItem>>
    suspend fun deleteLikeItem(itemId: Int): Result<Unit>
    suspend fun deleteSelectedLikeItems(itemIds: Set<Int>): Result<Unit>
    suspend fun loadMoreLikes(tab: String, startIndex: Int): Result<List<LikeItem>>
}