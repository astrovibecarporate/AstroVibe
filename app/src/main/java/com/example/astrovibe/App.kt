package com.example.astrovibe

import android.app.Application
import com.example.astrovibe.data.utils.Utils
import com.google.android.libraries.places.api.Places
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application(){
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, Utils.API_KEY)
        }
    }
}
