package com.example.tenshoku_and.data.remote

import com.example.tenshoku_and.data.model.UserResponse
import retrofit2.Response
import retrofit2.http.GET

interface UserApiService {
    @GET("/users")
    suspend fun getUsers(): Response<List<UserResponse>>
}