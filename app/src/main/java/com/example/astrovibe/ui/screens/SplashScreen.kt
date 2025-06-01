// SplashScreen.kt
package com.example.astrovibe.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.astrovibe.ui.UserAuthViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val viewModel: UserAuthViewModel = hiltViewModel()
    val auth = FirebaseAuth.getInstance()

    LaunchedEffect(Unit) {
        delay(2000) // Show splash for 2 seconds

        val currentUser = auth.currentUser
        if (currentUser != null && viewModel.isUserOnboard()) { // User has logged and Onboarded
            navigateHomePage(navController,viewModel.isUserAstrologer())
        } else if (currentUser != null){ // User has firebase logged in but not onboarded
            if(viewModel.isUserAstrologer()){
                navController.navigate("astrologer_registration/${auth.currentUser?.phoneNumber ?: ""}") {
                    popUpTo("auth") { inclusive = true }
                }
            }else {
                navController.navigate("user_registration/${auth.currentUser?.phoneNumber ?: ""}") {
                    popUpTo("auth") { inclusive = true }
                }
            }
        }else { // User has not firebase logged in
            navController.navigate("auth") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFB74D)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "AstroVibe 🔮",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}
