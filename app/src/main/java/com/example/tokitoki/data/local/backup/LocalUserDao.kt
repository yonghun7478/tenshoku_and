package com.example.tokitoki.data.local.backup

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalUserDao {
    @Query("SELECT * FROM localusers")
    fun getUsersFlow(): Flow<List<LocalUserEntity>>

    @Query("SELECT * FROM localusers")
     fun getUsersList(): List<LocalUserEntity>

    @Query("SELECT * FROM localusers WHERE id = :userId")
    suspend fun getUserById(userId: Int): LocalUserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: LocalUserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<LocalUserEntity>)

    @Update
    suspend fun updateUser(user: LocalUserEntity)

    @Delete
    suspend fun deleteUser(user: LocalUserEntity)

    @Query("DELETE FROM localusers WHERE id = :userId")
    suspend fun deleteUserById(userId: Int)
}