package com.example.astrovibe.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.astrovibe.ui.UserViewModel
import java.time.format.DateTimeFormatter
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.NavController


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserProfileScreen(navController: NavController) {
    val viewModel: UserViewModel = hiltViewModel()
    val user = viewModel.getUserLocalData()

    val numerologyNumber = 2
        /*user.numerology.let {
        if (it in 1..9) it else 0
    }*/

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Name & Age
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Name", tint = Color(0xFF388E3C))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${user.name}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color(0xFF2E7D32)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Email
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Icon(Icons.Default.Email, contentDescription = "Email", tint = Color(0xFF1976D2))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = user.email.toString(), fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Contact Number
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Icon(Icons.Default.Phone, contentDescription = "Contact", tint = Color(0xFF00796B))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = user.contactNumber, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Place
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = "Place", tint = Color(0xFFD84315))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = user.birthLocation.toString(), fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))

            // DOB and Time of Birth
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarToday, contentDescription = "DOB", tint = Color(0xFF5D4037))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = user.dateOfBirth?.format(DateTimeFormatter.ofPattern("dd MMM yyyy")).toString(),
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Icon(Icons.Default.AccessTime, contentDescription = "Time of Birth", tint = Color(0xFF6A1B9A))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = user.timeOfBirth?.format(DateTimeFormatter.ofPattern("HH:mm:ss")).toString(),
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Gender
            Text(
                text = "Gender: ${user.gender}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF424242)
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Wallet balance with icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.AccountBalanceWallet,
                    contentDescription = "Wallet",
                    tint = Color(0xFFFFA000),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Wallet Balance: ₹${"%.2f".format(user.walletBalance)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFFF57C00)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (numerologyNumber != 0) {
                // Numerology display with emoji size
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Text(
                        text = "🔮",
                        fontSize = 40.sp,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Column {
                        Text(
                            text = "Numerology Number: $numerologyNumber",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color(0xFF4A148C)
                        )
                        Text(
                            text = getTraits(numerologyNumber),
                            fontSize = 16.sp,
                            color = Color(0xFF6A1B9A)
                        )
                    }
                }
            }
            LogoutButton(navController)
        }
    }
}



@Composable
fun LogoutButton(navController: NavController) {
    Button(
        onClick = {
            FirebaseAuth.getInstance().signOut()
            navController.navigate("auth") {
                popUpTo(0)
                launchSingleTop = true
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
    ) {
        Text(text = "Logout", color = Color.White)
    }
}



