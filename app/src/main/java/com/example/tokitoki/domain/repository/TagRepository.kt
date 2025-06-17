package com.example.tokitoki.domain.repository

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.model.IdList
import com.example.tokitoki.domain.model.MainHomeTag
import com.example.tokitoki.domain.model.TagType

interface TagRepository {
    suspend fun getTagTypeList(): List<TagType>
    suspend fun getTagsByType(tagType: TagType): Result<List<MainHomeTag>>
    suspend fun getTags(tagIds : List<Int>): List<MainHomeTag>
    suspend fun getAllTags(): Result<List<MainHomeTag>>
    fun isTagSubscribed(tagId: String): Boolean
    suspend fun getTagSubscribers(tagId: String, cursor: String?, limit: Int): ResultWrapper<IdList>
    suspend fun getUserSubscribedTags(userId: String): Result<List<MainHomeTag>>
    suspend fun subscribeTag(tagId: String): Result<Unit>
    suspend fun unsubscribeTag(tagId: String): Result<Unit>
}