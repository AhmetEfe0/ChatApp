package com.ahmetefeozenc.chatappre.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahmetefeozenc.chatappre.classes.ChatMessage
import com.ahmetefeozenc.chatappre.repository.ChatRepository

class ChatViewModel : ViewModel() {
    private val repository = ChatRepository()
    private var otherUserEmail: String? = null
    private val _chatMessages = MutableLiveData<List<ChatMessage>>()
    val chatMessages: LiveData<List<ChatMessage>> = _chatMessages


    fun sendMessage(message: ChatMessage) {
        repository.sendMessage(message, {
            // Başarılı durumu işle
        }, { errorMessage ->
            // Hata durumunu işle
        })
    }

    fun loadChatMessages(currentUserEmail: String, otherUserEmail: String) {
        this.otherUserEmail = otherUserEmail
        repository.startListeningForChatMessages({ messages ->
            // Geçerli kullanıcı ve diğer kullanıcı e-postasına göre mesajları filtrele
            val filteredMessages = messages.filter { it ->
                (it.senderMail == currentUserEmail && it.receiverMail == otherUserEmail) ||
                        (it.senderMail == otherUserEmail && it.receiverMail == currentUserEmail)
            }
            _chatMessages.value = filteredMessages
        }, { errorMessage ->
            // Hata durumunu işle
        })
    }
    fun startListeningForChatMessages(currentUserEmail: String, otherUserEmail: String) {
        repository.startListeningForChatMessages({ messages ->
            val filteredMessages = messages.filter { it ->
                (it.senderMail == currentUserEmail && it.receiverMail == otherUserEmail) ||
                        (it.senderMail == otherUserEmail && it.receiverMail == currentUserEmail)
            }
            _chatMessages.value = filteredMessages
        }, { errorMessage ->
            // Hata durumunu işle
        })
    }
    // Sohbet mesajlarını dinlemeyi durdurmak için bir fonksiyon (aktivite yok edildiğinde bunu çağırın)
    fun stopListeningForChatMessages() {
        repository.stopListeningForChatMessages()
    }

    // ViewModel silindiğinde sohbet mesajlarını dinlemeyi durdurmak için onCleared'i geçersiz kılın
    override fun onCleared() {
        super.onCleared()
        stopListeningForChatMessages()
    }

}