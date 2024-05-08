package com.ahmetefeozenc.chatappre.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahmetefeozenc.chatappre.repository.AddChatRepository

class AddChatViewModel : ViewModel() {

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message
    private val addChatRepository = AddChatRepository()

    fun addChat(currentUserEmail: String, otherUserEmail: String) {
        if (addChatRepository.isValidEmail(otherUserEmail)) {
            addChatRepository.checkIfUserExistsInFirestore(otherUserEmail,
                onSuccess = {
                    addChatRepository.checkIfChatRoomExists(currentUserEmail, otherUserEmail,
                        onFailure = {
                            // Sohbet odası zaten var, işlemi tamamlayabilirsiniz.
                            _message.value = "Sohbet odası zaten mevcut."
                        },
                        onSuccess = {
                            // Sohbet odası yok, yeni bir sohbet odası oluşturun.
                            addChatRepository.createChatRoom(currentUserEmail, otherUserEmail,
                                onSuccess = {
                                    _message.value = "Yeni sohbet odası oluşturuldu."

                                },
                                onFailure = { errorMessage ->
                                    _message.value = errorMessage
                                }
                            )
                        }
                    )
                },
                onFailure = { errorMessage ->
                    _message.value = errorMessage
                }
            )
        } else {
            _message.value = "Geçersiz e-posta adresi"
        }

    }
}
