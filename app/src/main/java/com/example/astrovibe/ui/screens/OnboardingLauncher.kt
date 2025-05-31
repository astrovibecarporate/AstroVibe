// OnboardingLauncher.kt
package com.example.astrovibe.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.astrovibe.data.utils.Constants.ASTROLOGER_PHONE_NUMBERS_CONFIG
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.firebase.remoteconfig.remoteConfig
import kotlinx.coroutines.tasks.await

@Composable
fun OnboardingLauncher(
    navController: NavController,
    userPhoneNumber: String,
) {
    val remoteConfig = remember { Firebase.remoteConfig }
    var isLoading by remember { mutableStateOf(true) }
    var showAstroOnboarding by remember { mutableStateOf(false) }

    LaunchedEffect(userPhoneNumber) {
        if (userPhoneNumber.isBlank()) {
            navController.navigate("auth") {
                popUpTo("splash") { inclusive = true }
            }
            return@LaunchedEffect
        }

    }

    if (isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LaunchedEffect(showAstroOnboarding) {
            if (showAstroOnboarding) {
                navController.navigate("astrologer_registration/${userPhoneNumber}") {
                    popUpTo("splash") { inclusive = true }
                }
            } else {
                navController.navigate("user_registration/${userPhoneNumber}") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }
    }
}
