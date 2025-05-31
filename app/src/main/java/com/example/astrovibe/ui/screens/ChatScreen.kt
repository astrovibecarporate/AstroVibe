package com.example.astrovibe.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.astrovibe.ui.ChatViewModel
import androidx.compose.foundation.lazy.items
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ChatScreen(
    navController: NavController,
    astrologerId: String,
) {
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    Log.e("FIREBASE_ASHISH", "currentUserId $currentUserId")
    val conversationId =  "${astrologerId}_${currentUserId}_${Timestamp.now()}"
    val viewModel: ChatViewModel = hiltViewModel()
    val messages = viewModel.messages
    val messageText = viewModel.messageText
    val sessionEnded = viewModel.sessionEnded

    LaunchedEffect(Unit) {
        viewModel.listenToMessages(conversationId)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f).padding(8.dp),
            reverseLayout = false
        ) {
            items(messages) { msg ->
                val isMine = msg.senderId == currentUserId
                Text(
                    text = msg.text,
                    modifier = Modifier
                        .padding(4.dp)
                        .align(if (isMine) Alignment.End else Alignment.Start),
                    color = if (isMine) Color.Blue else Color.Black
                )
            }
        }

        if (sessionEnded) {
            Text(
                "Session Ended",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )
        } else {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                TextField(
                    value = messageText,
                    onValueChange = { viewModel.messageText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Type your message") }
                )
                IconButton(onClick = { viewModel.sendMessage(conversationId) }) {
                    Icon(Icons.Default.Send, contentDescription = "Send")
                }
            }

            Button(
                onClick = { viewModel.endSession(conversationId) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("End Session")
            }
        }
    }
}
