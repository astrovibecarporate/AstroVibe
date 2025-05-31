package com.example.astrovibe.data.utils

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig

object RemoteConfigManager {
    private val remoteConfig = Firebase.remoteConfig

    fun fetchAstrologerOptions(
        onSuccess: (languages: List<String>, knowledge: List<String>) -> Unit,
        onFailure: () -> Unit
    ) {
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val langs = remoteConfig.getString("astrologer_languages")
                val know = remoteConfig.getString("astrologer_knowledge")
                val languageList = langs.split(",").map { it.trim() }
                val knowledgeList = know.split(",").map { it.trim() }
                onSuccess(languageList, knowledgeList)
            } else onFailure()
        }
    }
}
