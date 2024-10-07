package com.example.tokitoki.domain.repository

import com.example.tokitoki.data.local.UserInterestWithCategory

interface UserInterestRepository {
    suspend fun getUserInterests(categoryId : Int): List<UserInterestWithCategory>
}