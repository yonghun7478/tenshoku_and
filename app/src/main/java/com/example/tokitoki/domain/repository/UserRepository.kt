package com.example.tokitoki.domain.repository

import com.example.tokitoki.common.ResultWrapper
import com.example.tokitoki.domain.model.UserList

interface UserRepository {
    suspend fun getUsers(cursor: String?, limit: Int, orderBy: String): ResultWrapper<UserList>
}
