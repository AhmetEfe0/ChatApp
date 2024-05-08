package com.ahmetefeozenc.chatappre.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ahmetefeozenc.chatappre.databinding.ActivityLoginBinding
import com.ahmetefeozenc.chatappre.repository.UserRepository

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userRepository: UserRepository
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userRepository = UserRepository()
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Oturum kontrolü
        if (isLoggedIn()) {
            val email = sharedPreferences.getString("email", "")
            if (!email.isNullOrEmpty()) {
                navigateToMainActivity(email)
                return
            }
        }

        binding.girisButton.setOnClickListener {
            val email = binding.emailText.text.toString()
            val password = binding.sifreText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(applicationContext, "Lütfen e-posta ve şifrenizi girin", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        userRepository.loginUser(email, password,
            onSuccess = {
                // Oturum bilgilerini SharedPreferences'e kaydet
                sharedPreferences.edit().apply {
                    putString("email", email)
                    apply()
                }
                navigateToMainActivity(email)
            },
            onFailure = { errorMessage ->
                Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_LONG).show()
            }
        )
    }

    private fun isLoggedIn(): Boolean {
        return sharedPreferences.contains("email")
    }

    private fun navigateToMainActivity(email: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
        finish()
    }
}
