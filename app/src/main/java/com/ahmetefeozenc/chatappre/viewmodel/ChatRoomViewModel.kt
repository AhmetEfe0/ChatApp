package com.ahmetefeozenc.chatappre.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahmetefeozenc.chatappre.classes.ChatRoomClass
import com.google.firebase.firestore.FirebaseFirestore

class ChatRoomViewModel : ViewModel() {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val _chatRooms = MutableLiveData<List<ChatRoomClass>>()
    val chatRooms: LiveData<List<ChatRoomClass>> = _chatRooms

    fun fetchChatRooms(email: String?) {
        firestore.collection("chatRooms")
            .whereArrayContains("users", email!!)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    // Hata durumunda işlemler
                    exception.printStackTrace()
                    return@addSnapshotListener
                }

                val chatRoomsList = mutableListOf<ChatRoomClass>()
                if (snapshot != null) {
                    for (document in snapshot.documents) {
                        val users = document["users"] as ArrayList<String>
                        val otherUser = users.firstOrNull { it != email }
                        chatRoomsList.add(ChatRoomClass(otherUser!!))
                    }
                }
                _chatRooms.value = chatRoomsList
            }
    }
}