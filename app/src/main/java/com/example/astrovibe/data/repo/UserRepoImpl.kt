package com.example.astrovibe.data.repo

import com.example.astrovibe.data.models.Astrologer
import com.example.astrovibe.data.models.Resource
import com.example.astrovibe.data.models.User
import com.example.astrovibe.data.remote.UserApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepoImpl @Inject constructor(private val userApiService: UserApiService) : UserRepo {
    override suspend fun createUser(user: User): Flow<Resource<User>> = flow {
        emit(Resource.Loading)
        val response = userApiService.createUser(user)
        if (response.isSuccessful) {
            val responseData = response.body()
            if (responseData != null) {
                emit(Resource.Success(responseData))
            } else {
                emit(Resource.Error("Failed to create user"))
            }

        } else {
            emit(Resource.Error(response.message().orEmpty()))
        }
    }.catch { e ->
        emit(Resource.Error("Exception: ${e.localizedMessage}"))
    }

    override suspend fun getUser(userId: String): Flow<Resource<User>> = flow {
        emit(Resource.Loading)
        val response = userApiService.getUser(userId)
        if (response.isSuccessful) {
            val responseData = response.body()
            if (responseData != null) {
                emit(Resource.Success(responseData))
            } else {
                emit(Resource.Error("Failed to get the user details"))
            }

        } else {
            emit(Resource.Error(response.message().orEmpty()))
        }
    }.catch { e ->
        emit(Resource.Error("Exception: ${e.localizedMessage}"))
    }


}