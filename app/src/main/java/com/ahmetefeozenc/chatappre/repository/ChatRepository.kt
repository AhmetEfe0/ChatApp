package com.ahmetefeozenc.chatappre.repository

import com.ahmetefeozenc.chatappre.classes.ChatMessage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ListenerRegistration

class ChatRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private var messageListener: ListenerRegistration? = null

    // Gerçek zamanlı sohbet mesajlarını dinlemek için bir fonksiyon
    fun startListeningForChatMessages(onChatMessageUpdate: (List<ChatMessage>) -> Unit, onFailure: (String) -> Unit) {
        // Sohbet mesajlarını almak için bir sorgu oluştur
        val sorgu = firestore.collection(MESSAGES_CHILD)
            .orderBy("date", Query.Direction.ASCENDING)

        // Gerçek zamanlı güncellemeleri dinlemeye başla
        messageListener = sorgu.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                onFailure(exception.message ?: "Bilinmeyen bir hata oluştu")
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val messages = mutableListOf<ChatMessage>()
                for (doc in snapshot.documents) {
                    val message = doc.toObject(ChatMessage::class.java)
                    message?.let { messages.add(it) }
                }
                onChatMessageUpdate(messages)
            }
        }
    }

    // Sohbet mesajlarını dinlemeyi durdurmak için bir fonksiyon
    fun stopListeningForChatMessages() {
        messageListener?.remove()
    }

    // Mesaj göndermek için bir fonksiyon
    fun sendMessage(message: ChatMessage, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        firestore.collection(MESSAGES_CHILD)
            .add(message)
            .addOnSuccessListener { documentReference ->
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Bilinmeyen bir hata oluştu")
            }
    }

    companion object {
        const val MESSAGES_CHILD = "messages"
    }
}
