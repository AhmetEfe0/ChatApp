package com.ahmetefeozenc.chatappre.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ahmetefeozenc.chatappre.activity.ChatActivity
import com.ahmetefeozenc.chatappre.classes.ChatRoomClass
import com.ahmetefeozenc.chatappre.databinding.RecylerviewChatroomItemBinding
import com.google.firebase.firestore.FirebaseFirestore

class ChatRoomControlAdapter(private val currentUserEmail: String) :
    ListAdapter<ChatRoomClass, ChatRoomControlAdapter.ViewHolder>(ChatRoomDiffCallback()) {

    class ViewHolder(val binding: RecylerviewChatroomItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecylerviewChatroomItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatRoom = getItem(position)
        holder.binding.isimText.text = chatRoom.name

        val db = FirebaseFirestore.getInstance()

        holder.binding.chatroomitemcard.setOnClickListener() {
            val otherUserEmail = chatRoom.name

            val intent = Intent(holder.itemView.context, ChatActivity::class.java)
            intent.putExtra("currentUserEmail", currentUserEmail)
            intent.putExtra("otherUserEmail", otherUserEmail)
            // onBindViewHolder içinde doğru sohbet odası ID'sini gönder
            db.collection("chatRooms")
                .whereIn(
                    "users", listOf(
                        listOf(currentUserEmail, otherUserEmail),
                        listOf(otherUserEmail, currentUserEmail)
                    )
                )
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val chatRoomId = documents.documents[0].id
                        intent.putExtra("chatroomId", chatRoomId)
                        holder.itemView.context.startActivity(intent)
                    } else {
                        // Belirtilen kullanıcılar arasında sohbet odası yoksa burada bir işlem yapılabilir
                    }
                }
                .addOnFailureListener { exception ->
                    // Hata durumunda yapılacak işlemler
                    exception.printStackTrace()
                }
        }
    }

    class ChatRoomDiffCallback : DiffUtil.ItemCallback<ChatRoomClass>() {
        override fun areItemsTheSame(oldItem: ChatRoomClass, newItem: ChatRoomClass): Boolean {
            return oldItem.name == newItem.name // Assuming name is unique identifier
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: ChatRoomClass, newItem: ChatRoomClass): Boolean {
            return oldItem == newItem
        }
    }
    fun updateChatRooms(chatRooms: List<ChatRoomClass>) {
        this.submitList(chatRooms)
        notifyDataSetChanged()
    }

}