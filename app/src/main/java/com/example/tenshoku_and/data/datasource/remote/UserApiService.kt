package com.example.tenshoku_and.data.datasource.remote

import com.example.tenshoku_and.domain.model.User
import retrofit2.Response
import retrofit2.http.GET

interface UserApiService {
    @GET("/users")
    suspend fun getUsers(): Response<User>
}