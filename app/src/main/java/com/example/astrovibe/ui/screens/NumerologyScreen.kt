package com.example.astrovibe.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.astrovibe.ui.components.DOBInputField
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment

import androidx.compose.ui.draw.shadow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumerologyScreen() {
    var name by remember { mutableStateOf("") }
    val dobState = remember { mutableStateOf(TextFieldValue("")) }
    var result by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Numerology Calculator",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 28.sp,
            color = Color(0xFF4A148C), // Deep Purple
            modifier = Modifier.padding(bottom = 24.dp)
        )

        if (name.isBlank() && dobState.value.text.isBlank() && result.isEmpty()) {
            // Interactive Welcome UI
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .shadow(8.dp, CircleShape)
                    .background(Color(0xFFEDE7F6), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🔮",
                    fontSize = 72.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Welcome to Numerology! \nEnter your details below to reveal your destiny.",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF311B92), // Dark Purple
                lineHeight = 28.sp,
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))
        }

        // Form inputs
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter your full name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF7B1FA2),
                focusedLabelColor = Color(0xFF7B1FA2)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        DOBInputField(dobState) // Your existing DOB input with automatic `-` formatting

        if (error.isNotEmpty()) {
            Text(text = error, color = Color(0xFFD32F2F), modifier = Modifier.padding(top = 4.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val dobPattern = Regex("""\d{2}-\d{2}-\d{4}""")
                if (name.isBlank()) {
                    error = "Please enter your full name"
                    result = ""
                } else if (!dobPattern.matches(dobState.value.text)) {
                    error = "Please enter DOB in dd-MM-yyyy format"
                    result = ""
                } else {
                    error = ""
                    val numerologyNumber = calculateNumerologyNumber(name, dobState.value.text)
                    result = "Your Numerology Number is $numerologyNumber\n\nTraits: ${getTraits(numerologyNumber)}"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7B1FA2))
        ) {
            Text(text = "Calculate", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (result.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Numerology Icon",
                        tint = Color(0xFFFFA000),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = result,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF6D4C41)
                    )
                }
            }
        }
    }
}



fun calculateNumerologyNumber(name: String, dob: String): Int {
    val nameSum = name.filter { it.isLetter() }
        .uppercase()
        .map { charToNumber(it) }
        .sum()
    val dobSum = dob.filter { it.isDigit() }.map { it.toString().toInt() }.sum()
    val total = nameSum + dobSum
    return reduceToSingleDigit(total)
}

fun charToNumber(char: Char): Int {
    return when (char) {
        in listOf('A', 'J', 'S') -> 1
        in listOf('B', 'K', 'T') -> 2
        in listOf('C', 'L', 'U') -> 3
        in listOf('D', 'M', 'V') -> 4
        in listOf('E', 'N', 'W') -> 5
        in listOf('F', 'O', 'X') -> 6
        in listOf('G', 'P', 'Y') -> 7
        in listOf('H', 'Q', 'Z') -> 8
        in listOf('I', 'R') -> 9
        else -> 0
    }
}

fun reduceToSingleDigit(num: Int): Int {
    var number = num
    while (number >= 10) {
        number = number.toString().map { it.toString().toInt() }.sum()
    }
    return number
}

fun getTraits(number: Int): String {
    return when (number) {
        1 -> "Leader, Independent, Ambitious"
        2 -> "Diplomatic, Friendly, Sensitive"
        3 -> "Creative, Joyful, Expressive"
        4 -> "Practical, Reliable, Disciplined"
        5 -> "Adventurous, Energetic, Curious"
        6 -> "Responsible, Caring, Harmonious"
        7 -> "Analytical, Spiritual, Introspective"
        8 -> "Powerful, Successful, Ambitious"
        9 -> "Compassionate, Humanitarian, Generous"
        else -> "Unknown"
    }
}
