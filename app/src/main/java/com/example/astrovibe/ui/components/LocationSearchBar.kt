package com.example.astrovibe.ui.components

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.*
import com.google.android.libraries.places.api.net.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*

@Composable
fun LocationSearchBar(
    context: Context = LocalContext.current,
    onLocationSelected: (Address) -> Unit
) {
    val placesClient = remember { Places.createClient(context) }

    var query by remember { mutableStateOf("") }
    var suggestions by remember { mutableStateOf(emptyList<AutocompletePrediction>()) }

    LaunchedEffect(query) {
        if (query.length > 2) {
            val token = AutocompleteSessionToken.newInstance()
            val request = FindAutocompletePredictionsRequest.builder()
                .setSessionToken(token)
                .setQuery(query)
                .build()

            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { suggestions = it.autocompletePredictions }
                .addOnFailureListener {
                    Log.d("LocationSearchBar", "Suggestions: failed $it")
                    suggestions = emptyList()
                }
        } else {
            suggestions = emptyList()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search location") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        AnimatedVisibility(
            visible = suggestions.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                suggestions.forEach { prediction ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                query = prediction.getFullText(null).toString()
                                fetchLatLngFromPlaceId(
                                    context = context,
                                    placesClient = placesClient,
                                    placeId = prediction.placeId,
                                    onLocationSelected = onLocationSelected
                                )
                                suggestions = emptyList()
                            },
                        shape = RoundedCornerShape(10.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = Color(0xFF4285F4)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = prediction.getFullText(null).toString(),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}




fun fetchLatLngFromPlaceId(
    context: Context,
    placesClient: PlacesClient,
    placeId: String,
    onLocationSelected: (Address) -> Unit
) {
    val request = FetchPlaceRequest.builder(placeId, listOf(Place.Field.LAT_LNG)).build()

    placesClient.fetchPlace(request)
        .addOnSuccessListener { response ->
            val latLng = response.place.latLng
            if (latLng != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val geocoder = Geocoder(context, Locale.getDefault())
                        val addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                        val address = addressList?.firstOrNull()

                        address?.let {
                            withContext(Dispatchers.Main) {
                                onLocationSelected(it)
                            }
                        }
                    } catch (e: IOException) {
                        Log.e("Geocoder", "Failed to get address: ${e.message}")
                    }
                }
            }
        }
        .addOnFailureListener {
            Log.d("LocationSearchBar", "Fetch place failed $it")
        }
}
