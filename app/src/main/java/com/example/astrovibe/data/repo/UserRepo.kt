package com.example.astrovibe.data.repo

import com.example.astrovibe.data.models.Astrologer
import com.example.astrovibe.data.models.Resource
import com.example.astrovibe.data.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepo {
    suspend fun createUser(user: User): Flow<Resource<User>>
    suspend fun getUser(userId: String): Flow<Resource<User>>
}