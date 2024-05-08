package com.ahmetefeozenc.chatappre.repository

import com.google.firebase.firestore.FirebaseFirestore

class AddChatRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun checkIfUserExistsInFirestore(otherUserEmail: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        // Kullanıcılar koleksiyonuna erişim
        val usersCollection = firestore.collection("users")

        // Verilen e-posta adresini içeren belgeleri al
        usersCollection
            .whereEqualTo("email", otherUserEmail)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    onFailure("Belirtilen e-posta adresine sahip bir kullanıcı bulunamadı")
                } else {
                    onSuccess()
                }
            }
            .addOnFailureListener { e ->
                onFailure("Kullanıcı sorgusu başarısız: $e")
            }
    }

    fun checkIfChatRoomExists(currentUserEmail: String, otherUserEmail: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        // Sohbet odaları koleksiyonuna erişim
        val chatRoomsCollection = firestore.collection("chatRooms")

        // Belirli bir kullanıcının sohbet odalarını sorgula
        chatRoomsCollection
            .whereArrayContains("users", currentUserEmail)
            .get()
            .addOnSuccessListener { currentUserRooms ->
                // Belirli bir kullanıcının sohbet odalarını aldıktan sonra, bu odalar içinde
                // diğer kullanıcının e-posta adresini içeren bir odanın olup olmadığını kontrol et
                for (room in currentUserRooms) {
                    val users = room["users"] as? List<String>
                    if (users != null && users.contains(otherUserEmail)) {
                        onFailure("Bu kullanıcılar arasında zaten bir sohbet odası bulunmaktadır.")
                        return@addOnSuccessListener
                    }
                }
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure("Sohbet odası kontrolü başarısız: $e")
            }
    }

    fun createChatRoom(currentUserEmail: String, otherUserEmail: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        // Sohbet odası oluşturulacak veritabanı yolu
        val chatRoomsCollection = firestore.collection("chatRooms")

        // Sohbet odası için bir ID oluştur
        val chatRoomId = chatRoomsCollection.document().id

        // Sohbet odası verilerini oluştur
        val chatRoomData = hashMapOf(
            "users" to listOf(currentUserEmail, otherUserEmail),
        )

        // Firestore'a sohbet odası verilerini yükle
        chatRoomsCollection.document(chatRoomId).set(chatRoomData)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure("Sohbet odası oluşturulamadı: $e")
            }
    }

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
