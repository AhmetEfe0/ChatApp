package com.ahmetefeozenc.chatappre.repository

import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    fun loginUser(email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        usersCollection
            .whereEqualTo("email", email)
            .whereEqualTo("sifre", password)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    onFailure("Hatalı mail ya da şifre")
                } else {
                    onSuccess()
                }
            }
            .addOnFailureListener { e ->
                onFailure("Kullanıcı sorgusu başarısız: $e")
            }
    }
}
