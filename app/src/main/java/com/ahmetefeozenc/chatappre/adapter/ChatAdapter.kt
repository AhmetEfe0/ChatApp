package com.ahmetefeozenc.chatappre.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ahmetefeozenc.chatappre.R
import com.ahmetefeozenc.chatappre.classes.ChatMessage
import com.ahmetefeozenc.chatappre.databinding.SentMessageItemBinding

class ChatAdapter(private val currentUserName: String?) : ListAdapter<ChatMessage, ChatAdapter.MessageViewHolder>(ChatDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SentMessageItemBinding.inflate(inflater, parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = getItem(position)
        holder.bind(message)
    }

    inner class MessageViewHolder(private val binding: SentMessageItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: ChatMessage) {
            binding.sentmessageText.text = message.msg
            binding.timeText.text=message.date
            setTextColor(message.senderMail, binding.sentmessageText)
        }
    }

    private fun setTextColor(userName: String?, textView: TextView) {
        if (currentUserName == userName && userName != null) {
            textView.setBackgroundResource(R.drawable.bg_send_message)
            textView.setTextColor(Color.WHITE)
        } else {
            textView.setBackgroundResource(R.drawable.bg_received_message)
            textView.setTextColor(Color.BLACK)
        }
    }


    class ChatDiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
        override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem.date == newItem.date // You can use any unique identifier
        }

        override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean {
            return oldItem == newItem // Assuming equals() is correctly implemented in ChatMessage
        }
    }
}
