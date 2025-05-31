package com.example.astrovibe.ui

import AstrologerListScreen
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.astrovibe.ui.astrologer.screens.AstrologerHomeScreen
import com.example.astrovibe.ui.screens.*
import com.example.astrovibe.ui.astrologer.screens.AstrologerRegistrationScreen

@Composable
fun NavGraph(navController: NavHostController, padding: PaddingValues) {
    NavHost(
        navController,
        startDestination = "splash",
        modifier = Modifier.padding(padding)
    ) {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("auth") {
            UserAuthScreen(navController)
        }
        composable("onboarding_launcher/{phoneNumber}") { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            OnboardingLauncher(navController, phoneNumber)
        }
        composable("user_registration/{phoneNumber}") { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            UserRegistrationScreen(navController, phoneNumber)
        }
        composable("astrologer_registration/{phoneNumber}") { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            AstrologerRegistrationScreen(navController, phoneNumber)
        }
        composable("location_search") {
            LocationSearchScreen(navController)
        }

        composable("astro_home") {
            AstrologerHomeScreen(navController)
        }
        composable("chat/{astrologerId}") { backStackEntry ->
            val astrologerId = backStackEntry.arguments?.getString("astrologerId") ?: ""
            ChatScreen(navController, astrologerId)
        }
        composable(BottomNavItem.Home.route) {
            AstrologerListScreen(navController)
        }
        composable(BottomNavItem.Numerology.route) {
            NumerologyScreen()
        }
        composable(BottomNavItem.Horoscope.route) {
            HoroscopeScreen()
        }
        composable(BottomNavItem.Remedies.route) {
            RemediesScreen()
        }
        composable(BottomNavItem.User.route) {
            UserProfileScreen(navController)
        }
    }
}
