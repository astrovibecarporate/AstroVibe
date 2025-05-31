package com.example.astrovibe.ui.screens

import android.app.DatePickerDialog
import android.location.Address
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.astrovibe.data.models.UserRegistrationData
import java.time.LocalDate
import java.time.LocalTime
import android.util.Patterns
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.astrovibe.data.models.BirthLocation
import com.example.astrovibe.data.models.Resource
import com.example.astrovibe.data.models.User
import com.example.astrovibe.ui.UserViewModel
import com.example.astrovibe.ui.components.ClearableOutlinedTextField
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserRegistrationScreen(navController: NavController, phoneNumber: String) {

    val viewModel: UserViewModel = hiltViewModel()
    var name by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf<String?>(null) }
    var email by remember { mutableStateOf("") }

    var selectedDOB by remember { mutableStateOf<LocalDate?>(null) }
    var selectedTOB by remember { mutableStateOf(LocalTime.MIDNIGHT) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val context = LocalContext.current

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    val isFormValid =
        name.isNotBlank() && gender != null && email.isNotBlank() && isEmailValid(email)

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val json = savedStateHandle?.getLiveData<String>("selected_address")?.observeAsState()

    // Define default empty BirthLocation
    val defaultAddress = BirthLocation("", "", "", 0.0, 0.0)

    // Use remember to parse JSON into BirthLocation, or fallback to default
    val selectedAddress = remember(json?.value) {
        json?.value?.let { Gson().fromJson(it, BirthLocation::class.java) } ?: defaultAddress
    }

    // React when selectedAddress is updated (and not default)
    LaunchedEffect(json?.value) {
        if (json?.value != null) {
            savedStateHandle?.remove<String>("selected_address") // Clear saved state after use
        }
    }


    // Date Picker Dialog
    if (showDatePicker) {
        val today = java.util.Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                selectedDOB = LocalDate.of(year, month + 1, dayOfMonth)
                showDatePicker = false
            },
            selectedDOB?.year ?: today.get(java.util.Calendar.YEAR),
            selectedDOB?.monthValue?.minus(1) ?: today.get(java.util.Calendar.MONTH),
            selectedDOB?.dayOfMonth ?: today.get(java.util.Calendar.DAY_OF_MONTH)
        ).show()
    }

    // Time Picker Dialog
    if (showTimePicker) {
        val currentHour = selectedTOB.hour
        val currentMinute = selectedTOB.minute
        android.app.TimePickerDialog(
            context,
            { _, hour, minute ->
                selectedTOB = LocalTime.of(hour, minute).withSecond(0)
                showTimePicker = false
            },
            currentHour,
            currentMinute,
            true
        ).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("User Details", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(12.dp))

        ClearableOutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = "Full Name",
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        )
        Spacer(Modifier.height(12.dp))

        Text("Select Gender")
        Spacer(Modifier.height(6.dp))
        Row {
            listOf("Male", "Female", "Other").forEach { option ->
                FilterChip(
                    selected = gender == option,
                    onClick = { gender = option },
                    modifier = Modifier.padding(end = 8.dp),
                    label = { Text(option) }
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        ClearableOutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            isError = email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()
        )
        if (email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Text(
                "Invalid email format",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.Start)
            )
        }

        Spacer(Modifier.height(24.dp))

        Text("Birth Details (Optional)", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = { showDatePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.CalendarToday, contentDescription = "Select DOB")
            Spacer(Modifier.width(8.dp))
            Text(selectedDOB?.toString() ?: "Select Date of Birth")
        }
        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = { showTimePicker = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.AccessTime, contentDescription = "Select Time of Birth")
            Spacer(Modifier.width(8.dp))
            Text(selectedTOB.toString())
        }
        Spacer(Modifier.height(12.dp))

        // Place of Birth input (readonly, clickable to navigate)
        OutlinedTextField(
            value = selectedAddress.toString(),
            onValueChange = { /* no manual edit */ },
            label = { Text("Place of Birth") },
            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    Log.d("UserRegistrationScreen", "Place of Birth clicked")
                    navController.navigate("location_search")
                },
            readOnly = true
        )
        Spacer(Modifier.height(24.dp))

        val userState by viewModel.user.collectAsState()

        var isSubmitted by remember { mutableStateOf(false) }

        Button(
            onClick = {
                Log.e("UserRegistrationScreen", "Date ${selectedDOB.toString()}   time ${selectedTOB.toString()}")
                FirebaseAuth.getInstance().currentUser?.uid?.let { fid ->
                    val userData = User(
                        fid = fid,
                        name = name,
                        gender = gender ?: "",
                        email = email ?: "",
                        imageUrl = "",
                        contactNumber = phoneNumber.removePrefix("+91"),
                        dateOfBirth = selectedDOB.toString(),
                        timeOfBirth = selectedTOB.withSecond(0).toString(),
                        birthLocation = selectedAddress,
                        walletBalance = 0.0
                    )
                    isSubmitted = true
                    viewModel.createUser(userData)
                }
            },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }

        LaunchedEffect(userState) {
            if (isSubmitted && userState is Resource.Success) {
                navController.navigate("home") {
                    popUpTo("user_registration") { inclusive = true }
                }
            }
        }

    }
}
