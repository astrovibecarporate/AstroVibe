package com.example.astrovibe.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    var messages by mutableStateOf<List<Message>>(emptyList())
        private set

    var messageText by mutableStateOf("")

    var sessionEnded by mutableStateOf(false)
        private set

    fun listenToMessages(conversationId: String) {
        firestore.collection("conversations")
            .document(conversationId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, _ ->
                snapshot?.let {
                    messages = it.documents.mapNotNull { doc ->
                        doc.toObject(Message::class.java)
                    }
                }
            }

        // Check if session is already ended
        firestore.collection("conversations")
            .document(conversationId)
            .addSnapshotListener { snapshot, _ ->
                val active = snapshot?.getBoolean("isActive") ?: false
                sessionEnded = !active
            }
    }

    fun sendMessage(conversationId: String) {
        val senderId = auth.currentUser?.uid ?: return
        val message = Message(
            senderId = senderId,
            text = messageText,
            timestamp = Timestamp.now()
        )

        firestore.collection("conversations")
            .document(conversationId)
            .collection("messages")
            .add(message)
            .addOnSuccessListener { messageText = "" }
    }

    fun endSession(conversationId: String) {
        firestore.collection("conversations")
            .document(conversationId)
            .update(
                mapOf(
                    "isActive" to false,
                    "endedAt" to FieldValue.serverTimestamp(),
                    "endedBy" to "user"
                )
            )
    }
}

data class Message(
    val senderId: String = "",
    val text: String = "",
    val timestamp: Timestamp = Timestamp.now()
)
