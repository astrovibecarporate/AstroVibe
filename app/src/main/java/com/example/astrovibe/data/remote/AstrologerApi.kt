package com.example.astrovibe.data.remote

import com.example.astrovibe.data.models.Astrologer
import retrofit2.Response
import retrofit2.http.GET

interface AstrologerApi {
    @GET("/api/astrologers")
    suspend fun getAstrologers(): Response<List<Astrologer>>
}

