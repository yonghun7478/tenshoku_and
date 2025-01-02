package com.example.tokitoki.data.remote

import com.example.tokitoki.data.model.UserResponse
import retrofit2.Response
import retrofit2.http.GET

interface UserApiService {
    @GET("/users")
    suspend fun getUsers(): Response<List<UserResponse>>
}