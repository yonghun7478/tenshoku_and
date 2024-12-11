package com.example.tokitoki.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MyTagDao {

    // 태그 추가 (userId는 항상 0으로 설정)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMyTag(myTag: MyTagEntity): Long

    // 특정 태그 삭제 (userId는 항상 0)
    @Query("DELETE FROM my_tags WHERE tagId = :tagId")
    suspend fun deleteMyTag(tagId: Int)

    // my_tags 테이블에서 모든 tagId 가져오기
    @Query("SELECT tagId FROM my_tags")
    suspend fun getAllTagIds(): List<Int>

    // my_tags 테이블의 모든 데이터를 가져오기 (필요하다면)
    @Query("SELECT * FROM my_tags")
    suspend fun getAllMyTags(): List<MyTagEntity>

    // 전체 태그 삭제 (userId는 항상 0)
    @Query("DELETE FROM my_tags")
    suspend fun deleteAllTagsForUser()
}
