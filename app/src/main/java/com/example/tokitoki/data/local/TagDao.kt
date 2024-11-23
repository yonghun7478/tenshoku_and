package com.example.tokitoki.data.local

import androidx.room.Dao
import androidx.room.Query

@Dao
interface TagDao {
    @Query(
        "SELECT " +
                "tags.id AS tagId, " +
                "tags.title AS tagTitle, " +
                "tags.url AS tagUrl, " +
                "tags.categoryId AS categoryId, " +
                "categories.title AS categoryTitle " +
                "FROM tags " +
                "INNER JOIN categories ON categories.id = tags.categoryId " +
                "WHERE categories.id = :categoryId"
    )
    suspend fun getTagsWithCategory(categoryId: Int): List<TagWithCategoryEntity>


    @Query(
        "SELECT " +
                "tags.id AS tagId, " +
                "tags.title AS tagTitle, " +
                "tags.url AS tagUrl, " +
                "tags.categoryId AS categoryId, " +
                "categories.title AS categoryTitle " +
                "FROM tags " +
                "INNER JOIN categories ON categories.id = tags.categoryId " +
                "WHERE tags.id IN (:tagIds)"
    )
    suspend fun getTagsByIds(tagIds: List<Int>): List<TagWithCategoryEntity>
}
