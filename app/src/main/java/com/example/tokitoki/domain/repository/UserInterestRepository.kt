package com.example.tokitoki.domain.repository

import com.example.tokitoki.domain.model.UserInterest


interface UserInterestRepository {
    suspend fun getUserInterests(categoryId : Int): List<UserInterest>
}