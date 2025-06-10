package com.example.tokitoki.domain.repository

import com.example.tokitoki.data.model.TagType
import com.example.tokitoki.domain.model.MainHomeTag


interface TagRepository {
    suspend fun getTagTypeList(): List<TagType>
    suspend fun getTagsByType(tagType: TagType): Result<List<MainHomeTag>>
    suspend fun getTags(tagIds : List<Int>): List<MainHomeTag>
}