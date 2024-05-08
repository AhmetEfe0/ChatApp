package com.ahmetefeozenc.chatappre.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmetefeozenc.chatappre.adapter.ChatAdapter
import com.ahmetefeozenc.chatappre.classes.ChatMessage
import com.ahmetefeozenc.chatappre.databinding.ActivityChatBinding
import com.ahmetefeozenc.chatappre.viewmodel.ChatViewModel
import java.text.SimpleDateFormat
import java.util.*


class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var adapter: ChatAdapter
    lateinit var currentUserEmail:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUserEmail = intent.getStringExtra("currentUserEmail").toString()
        val otherUserEmail = intent.getStringExtra("otherUserEmail")

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = otherUserEmail // Başlık olarak otherUserEmail kullan
            setDisplayHomeAsUpEnabled(true)
        }

        setupViews()
        observeViewModel()

        viewModel.loadChatMessages(currentUserEmail!!, otherUserEmail!!)
        viewModel.startListeningForChatMessages(currentUserEmail, otherUserEmail)

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private fun setupViews() {
        adapter = ChatAdapter(currentUserEmail)

        binding.chatrecylerview.layoutManager = LinearLayoutManager(this)
        binding.chatrecylerview.adapter = adapter

        binding.chatmsgsendButton.setOnClickListener {
            sendMessage()
        }
        binding.msgText.setOnClickListener(){
            observeViewModel()
        }
    }

    private fun observeViewModel() {
        viewModel.chatMessages.observe(this) { messages ->
            adapter.submitList(messages)
            val itemCount = adapter.itemCount
            // Eğer item sayısı 0'dan büyükse, RecyclerView'ı en alta kaydır
            if (itemCount > 0) {
                binding.chatrecylerview.post {
                    binding.chatrecylerview.smoothScrollToPosition(itemCount - 0)
                }
            }
        }
    }

    private fun sendMessage() {
        val messageText = binding.msgText.text.toString().trim() // Trim ile baştaki ve sondaki boşlukları kaldır
        if (messageText.isNotEmpty()) { // Mesaj boş değilse devam et
            val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

            val message = ChatMessage(
                date = currentDate,
                msg = messageText,
                receiverMail = intent.getStringExtra("otherUserEmail"),
                senderMail = intent.getStringExtra("currentUserEmail"),
                time = currentTime
            )

            viewModel.sendMessage(message)
            binding.msgText.setText("")
            observeViewModel()
        } else {
            Toast.makeText(this, "Mesaj boş olamaz", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopListeningForChatMessages()
    }
}