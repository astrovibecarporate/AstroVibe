package com.example.astrovibe.data.remote

import com.example.astrovibe.data.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApiService {

    @POST("api/users")
    suspend fun createUser(@Body user: User): Response<User>

    @GET("api/users/{id}")
    suspend fun getUser(@Path("id") id: String): Response<User>

}