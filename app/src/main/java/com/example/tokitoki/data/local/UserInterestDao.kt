package com.example.tokitoki.data.local

import androidx.room.Dao
import androidx.room.Query

@Dao
interface UserInterestDao {
    @Query(
        "SELECT " +
                "user_interests.id AS interestId, " +
                "user_interests.title AS interestTitle, " +
                "user_interests.url AS interestUrl, " +
                "user_interests.categoryId AS categoryId, " +
                "categories.title AS categoryTitle " +
                "FROM user_interests " +
                "INNER JOIN categories ON categories.id = user_interests.categoryId " +
                "WHERE categories.id = :categoryId"
    )
    suspend fun getUserInterestsWithCategory(categoryId: Int): List<UserInterestWithCategoryEntity>
}
