package com.example.astrovibe.data.repo

import com.example.astrovibe.data.models.Astrologer
import com.example.astrovibe.data.models.Resource
import kotlinx.coroutines.flow.Flow

interface AstrologerRepo {
    suspend fun fetchAstrologers(): Flow<Resource<List<Astrologer>>>
}