package com.example.astrovibe.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.astrovibe.data.utils.AppPreferences
import com.example.astrovibe.data.utils.Constants.ASTROLOGER_PHONE_NUMBERS_CONFIG
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class UserAuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val appPreferences: AppPreferences
) : ViewModel() {

    private val preOnBoardedAstrologerList = mutableListOf<String>()

    init {
        Log.e("ASHISH_FIREBASE", "init called $preOnBoardedAstrologerList")
        getAllPreOnBoardedAstrologer()
    }

    fun isUserAstrologer(): Boolean {
        return appPreferences.isUserAstrologer()
    }

    fun isUserOnboard(): Boolean {
        Log.e("ASHISH_FIREBASE", "isUserOnboard called ${!appPreferences.getName().isNullOrEmpty()}  name ${appPreferences.getName()}")
        return !appPreferences.getName().isNullOrEmpty()
    }

    private fun getAllPreOnBoardedAstrologer() {
        viewModelScope.launch(Dispatchers.IO) {
            val remoteConfig = Firebase.remoteConfig
            val configSettings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = 3600
            }
            try {
                Log.e("ASHISH_FIREBASE", "try called $preOnBoardedAstrologerList")
                remoteConfig.setConfigSettingsAsync(configSettings).await()
                remoteConfig.setDefaultsAsync(mapOf(ASTROLOGER_PHONE_NUMBERS_CONFIG to "")).await()
                val activated = remoteConfig.fetchAndActivate().await()
                if (activated) {
                    val phonesCsv = remoteConfig.getString(ASTROLOGER_PHONE_NUMBERS_CONFIG)
                    val list = phonesCsv.split(",").map { it.trim() }
                    Log.e("FIREBASE_ASHISH", "phonesCsv $list $phonesCsv")
                    preOnBoardedAstrologerList.clear()
                    preOnBoardedAstrologerList.addAll(list)
                    val isPreOnboarded = auth.currentUser?.phoneNumber in preOnBoardedAstrologerList
                    appPreferences.setUserIfUserAstrologer(isPreOnboarded)
                }
            } catch (e: Exception) {
                // Optional: Log error
            }
        }
    }

}
