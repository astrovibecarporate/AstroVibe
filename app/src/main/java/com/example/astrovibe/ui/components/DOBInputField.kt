package com.example.astrovibe.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun DOBInputField(
    dobState: MutableState<TextFieldValue>
) {
    OutlinedTextField(
        value = dobState.value,
        onValueChange = { input ->
            val oldText = dobState.value.text
            val newText = input.text

            // Handle deletion smoothly by allowing user to delete dash as well
            if (newText.length < oldText.length) {
                // User pressed backspace
                dobState.value = input
                return@OutlinedTextField
            }

            val digits = newText.filter { it.isDigit() }.take(8)

            val formatted = buildString {
                for (i in digits.indices) {
                    append(digits[i])
                    if (i == 1 || i == 3) append('-')
                }
            }.take(10)

            dobState.value = TextFieldValue(
                text = formatted,
                selection = TextRange(formatted.length)
            )
        },
        label = { Text("Enter DOB (dd-MM-yyyy)") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

