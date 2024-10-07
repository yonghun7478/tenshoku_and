package com.example.tokitoki.data.repository

import com.example.tokitoki.data.local.UserInterestDao
import com.example.tokitoki.data.local.UserInterestWithCategory
import com.example.tokitoki.domain.repository.UserInterestRepository

class UserInterestRepositoryImpl(
    private val userInterestDao: UserInterestDao
) : UserInterestRepository {
    override suspend fun getUserInterests(categoryId : Int): List<UserInterestWithCategory> {
        return userInterestDao.getUserInterestsWithCategory(categoryId)
    }
}