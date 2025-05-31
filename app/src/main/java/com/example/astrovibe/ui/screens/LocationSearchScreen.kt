package com.example.astrovibe.ui.screens

import android.location.Address
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.astrovibe.data.models.BirthLocation
import com.example.astrovibe.ui.components.LocationSearchBar
import com.google.gson.Gson

@Composable
fun LocationSearchScreen(navController: NavController) {
    val context = LocalContext.current
    val gson = Gson()

    Log.d("UserRegistrationScreen", "LocationSearchScreen")

    LocationSearchBar(
        context = context,
        onLocationSelected = { address: Address ->
            val city = address.subAdminArea ?: address.locality ?: ""
            val state = address.adminArea ?: ""
            val country = address.countryName ?: ""
            val lat = address.latitude ?: 0.0
            val lon = address.longitude ?: 0.0

            val selectedAddress = BirthLocation(city, state, country, lat, lon)
            val json = gson.toJson(selectedAddress)

            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("selected_address", json)

            // Navigate back to UserRegistrationScreen
            navController.popBackStack()
        }
    )
}
