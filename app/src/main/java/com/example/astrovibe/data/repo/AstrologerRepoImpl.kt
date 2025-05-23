package com.example.astrovibe.data.repo

import com.example.astrovibe.data.models.Astrologer
import com.example.astrovibe.data.models.Resource
import com.example.astrovibe.data.remote.AstrologerApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AstrologerRepoImpl @Inject constructor(
    private val api: AstrologerApi
): AstrologerRepo {
    override suspend fun fetchAstrologers(): Flow<Resource<List<Astrologer>>> = flow {
        emit(Resource.Loading)
        val response = api.getAstrologers()
        if (response.isSuccessful) {
            val responseData = response.body()
            if(responseData!=null){
                emit(Resource.Success(responseData))
            }else{
                emit(Resource.Success(arrayListOf()))
            }

        } else {
            emit(Resource.Error(response.message().orEmpty()))
        }
    }.catch { e ->
        emit(Resource.Error("Exception: ${e.localizedMessage}"))
    }


}
