package com.example.tokitoki.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MyProfileDao {

    // 1. 프로필 가져오기 (항상 단일 레코드가 존재해야 함)
    @Query("SELECT * FROM my_profile WHERE id = :id LIMIT 1")
    suspend fun getProfile(id: Int = 0): MyProfileEntity?

    // 2. 프로필 삽입 또는 업데이트
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: MyProfileEntity)

    // 3. 특정 필드 업데이트 (예: 이름만 변경)
    @Query("UPDATE my_profile SET name = :name WHERE id = :id")
    suspend fun updateName(name: String, id: Int = 0)

    @Query("UPDATE my_profile SET birthDay = :birthDay WHERE id = :id")
    suspend fun updateBirthDay(birthDay: String, id: Int = 0)

    @Query("UPDATE my_profile SET mySelfSentence = :sentence WHERE id = :id")
    suspend fun updateMySelfSentence(sentence: String, id: Int = 0)

    @Query("UPDATE my_profile SET isMale = :isMale WHERE id = :id")
    suspend fun updateGender(isMale: Boolean, id: Int = 0)

    // 4. 프로필 삭제 (초기화)
    @Query("DELETE FROM my_profile WHERE id = :id")
    suspend fun deleteProfile(id: Int = 0)
}