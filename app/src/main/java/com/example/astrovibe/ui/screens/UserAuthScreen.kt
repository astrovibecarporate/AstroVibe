// UserAuthScreen.kt
package com.example.astrovibe.ui.screens

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.astrovibe.ui.UserAuthViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

@Composable
fun UserAuthScreen(navController: NavController) {

    val viewModel: UserAuthViewModel = hiltViewModel()
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    var phoneInput by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var verificationId by remember { mutableStateOf("") }
    var codeSent by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        // Auto Verification Flow like reading OTP SMS
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            auth.signInWithCredential(credential).addOnCompleteListener {
                if (it.isSuccessful) {
                    navigateToRegistration(navController, auth, viewModel.isUserAstrologer())
                }
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(context, "Verification failed: ${e.message}", Toast.LENGTH_SHORT).show()
            loading = false
        }

        override fun onCodeSent(verificationIdParam: String, token: PhoneAuthProvider.ForceResendingToken) {
            verificationId = verificationIdParam
            codeSent = true
            loading = false
        }
    }

    LaunchedEffect(Unit) {
        loading = true
        Log.e("ASHISH_FIREBASE", "LaunchedEffect called")
        auth.currentUser?.let {
            navigateHomePage(navController, viewModel.isUserAstrologer())
            return@LaunchedEffect
        }
        loading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!codeSent) {
            OutlinedTextField(
                value = phoneInput,
                onValueChange = {
                    if (it.length <= 10 && it.all { ch -> ch.isDigit() }) phoneInput = it
                },
                label = { Text("Phone Number") },
                leadingIcon = { Text("+91", modifier = Modifier.padding(start = 8.dp)) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (phoneInput.length != 10) {
                        Toast.makeText(context, "Enter 10-digit number", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    loading = true
                    val fullPhone = "+91$phoneInput"
                    val options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(fullPhone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(context as Activity)
                        .setCallbacks(callbacks)
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)
                },
                enabled = !loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Send OTP")
            }
        } else {
            OutlinedTextField(
                value = otp,
                onValueChange = { otp = it },
                label = { Text("Enter OTP") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val credential = PhoneAuthProvider.getCredential(verificationId, otp)
                    // Sign in with manual otp
                    auth.signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            navigateToRegistration(navController, auth, viewModel.isUserAstrologer())
                        } else {
                            Toast.makeText(context, "Invalid OTP", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Verify & Login")
            }
        }
    }
}


fun navigateToRegistration(navController: NavController,auth: FirebaseAuth,isPreOnboardedAstrologer: Boolean){
    if(isPreOnboardedAstrologer) {
        navController.navigate("astrologer_registration/${auth.currentUser?.phoneNumber ?: ""}") {
            popUpTo("auth") { inclusive = true }
        }
    }else{
        navController.navigate("user_registration/${auth.currentUser?.phoneNumber ?: ""}") {
            popUpTo("auth") { inclusive = true }
        }
    }
}

fun navigateHomePage(navController: NavController,isPreOnboardedAstrologer: Boolean){
    if(isPreOnboardedAstrologer) {
        navController.navigate("astro_home") { // TODO Astrologer home screen
            popUpTo("auth") { inclusive = true }
        }
    }else{
        navController.navigate("home") { // TODO  home screen
            popUpTo("auth") { inclusive = true }
        }
    }
}


