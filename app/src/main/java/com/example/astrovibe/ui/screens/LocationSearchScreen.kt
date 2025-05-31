package com.example.astrovibe.ui.screens

import android.location.Address
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.astrovibe.ui.components.LocationSearchBar

@Composable
fun LocationSearchScreen(navController: NavController) {
    val context = LocalContext.current
    Log.d("UserRegistrationScreen", "LocationSearchScreen")
    LocationSearchBar(
        context = context,
        onLocationSelected = { address: Address ->
            val addressString = address.getAddressLine(0) ?: ""
            // Pass the selected address back via savedStateHandle
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("selected_address", addressString)

            // Navigate back to UserRegistrationScreen
            navController.popBackStack()
        }
    )
}
