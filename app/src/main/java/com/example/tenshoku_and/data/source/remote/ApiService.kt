package com.example.tenshoku_and.data.source.remote

import com.example.tenshoku_and.data.model.ExampleData
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("endpoint")
    suspend fun getExampleData(): Response<ExampleData>
}