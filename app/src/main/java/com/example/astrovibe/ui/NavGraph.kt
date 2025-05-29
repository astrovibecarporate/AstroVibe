package com.example.astrovibe.ui

import AstrologerListScreen
import UserAuthScreen
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.astrovibe.ui.screens.BottomNavItem
import com.example.astrovibe.ui.screens.HoroscopeScreen
import com.example.astrovibe.ui.screens.NumerologyScreen
import com.example.astrovibe.ui.screens.RemediesScreen
import com.example.astrovibe.ui.screens.SplashScreen
import com.example.astrovibe.ui.screens.UserProfileScreen


@Composable
fun NavGraph(navController: NavHostController, padding: PaddingValues) {
    NavHost(
        navController,
    startDestination = BottomNavItem.Home.route,
    modifier = Modifier.padding(padding)
    ) {
        composable("splash") { SplashScreen(navController) }
        composable("auth") { UserAuthScreen(navController) }
        composable(BottomNavItem.Home.route) { AstrologerListScreen() }
        composable(BottomNavItem.Numerology.route) { NumerologyScreen() }
        composable(BottomNavItem.Horoscope.route) { HoroscopeScreen() }
        composable(BottomNavItem.Remedies.route) { RemediesScreen() }
        composable(BottomNavItem.User.route) { UserProfileScreen(navController) }
    }
}
