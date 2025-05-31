package com.example.astrovibe.ui.astrologer.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.astrovibe.data.utils.Constants.ASTROLOGER_KNOWLEDGE_CONFIG
import com.example.astrovibe.data.utils.Constants.ASTROLOGER_LANGUAGES_CONFIG
import com.example.astrovibe.data.utils.Utils
import com.example.astrovibe.data.utils.Utils.uploadImage
import com.example.astrovibe.ui.components.MultiSelectChips
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import kotlinx.coroutines.tasks.await

@Composable
fun AstrologerRegistrationScreen(navController: NavController, astrologerPhoneNumber: String) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var expertise by remember { mutableStateOf("") }
    var photoUrl by remember { mutableStateOf<String?>(null) }
    var uploading by remember { mutableStateOf(false) }
    var uploadProgress by remember { mutableStateOf(0f) }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }

    var allLanguages by remember { mutableStateOf<List<String>>(emptyList()) }
    var allKnowledge by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedLanguages by remember { mutableStateOf(setOf<String>()) }
    var selectedKnowledge by remember { mutableStateOf(setOf<String>()) }
    var selectedGender by remember { mutableStateOf<String?>(null) }
    var isLoadingConfig by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            val remoteConfig = Firebase.remoteConfig
            remoteConfig.fetchAndActivate().await()
            allLanguages = remoteConfig.getString(ASTROLOGER_LANGUAGES_CONFIG).split(",").map { it.trim() }
            allKnowledge = remoteConfig.getString(ASTROLOGER_KNOWLEDGE_CONFIG).split(",").map { it.trim() }
        } catch (e: Exception) {
            allLanguages = listOf("Hindi", "English")
            allKnowledge = listOf("Vedic", "Numerology")
        } finally {
            isLoadingConfig = false
        }
    }

    LaunchedEffect(selectedUri) {
        selectedUri?.let { uri ->
            uploading = true
            val compressedBitmap = Utils.compressImage(context, uri)
            if (compressedBitmap != null) {
                uploadImage(astrologerPhoneNumber,
                    compressedBitmap,
                    onProgress = { progress -> uploadProgress = progress },
                    onSuccess = {
                        photoUrl = it
                        uploading = false
                    },
                    onFailure = {
                        uploading = false
                    })
            } else {
                uploading = false
            }
        }
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        selectedUri = uri
    }

    if (isLoadingConfig) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = expertise,
            onValueChange = { expertise = it },
            label = { Text("Expertise") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Select Gender")
        Row {
            listOf("Male", "Female", "Other").forEach { gender ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    RadioButton(
                        selected = selectedGender == gender,
                        onClick = { selectedGender = gender }
                    )
                    Text(gender)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Languages Known")
        MultiSelectChips(
            options = allLanguages,
            selected = selectedLanguages,
            onSelectionChange = { selectedLanguages = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Astrology Knowledge")
        MultiSelectChips(
            options = allKnowledge,
            selected = selectedKnowledge,
            onSelectionChange = { selectedKnowledge = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (photoUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(photoUrl),
                contentDescription = "Uploaded Photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { launcher.launch("image/*") },
            enabled = !uploading
        ) {
            Text(if (uploading) "Uploading... ${(uploadProgress * 100).toInt()}%" else "Upload Photo")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                navController.navigate("astro_home") {
                    popUpTo("astrologer_registration") { inclusive = true }
                }
            },
            enabled = name.isNotBlank() &&
                    expertise.isNotBlank() &&
                    selectedLanguages.isNotEmpty() &&
                    selectedKnowledge.isNotEmpty() &&
                    selectedGender != null &&
                    astrologerPhoneNumber.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }
    }
}
