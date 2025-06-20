package com.example.astrovibe.ui.screens

import AstrologerListScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.astrovibe.ui.NavGraph

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    data object Home : BottomNavItem("home", "Home", Icons.Default.Home)
    data object Numerology : BottomNavItem("numerology", "Numerology", Icons.Default.Calculate)
    data object Horoscope : BottomNavItem("horoscope", "Horoscope", Icons.Default.CalendarToday)
    data object Remedies : BottomNavItem("remedies", "Remedies", Icons.Default.Spa)
    data object User : BottomNavItem("user", "User", Icons.Default.Person)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination?.route
    val showBottomBar = currentDestination in listOf(BottomNavItem.Home.route, BottomNavItem.Numerology.route, BottomNavItem.Horoscope.route, BottomNavItem.Remedies.route, BottomNavItem.User.route)

    Scaffold(
        bottomBar = {
            if(showBottomBar)
            BottomNavigationBar(navController)
        },
        topBar = {
            TopAppBar(
                title = { Text("AstroVibe", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFFFFB74D)
                )
            )
        }
    ) { padding ->
        NavGraph(navController, padding)
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Numerology,
        BottomNavItem.Horoscope,
       // BottomNavItem.Remedies,
        BottomNavItem.User,
    )
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }

}




