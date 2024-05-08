package com.ahmetefeozenc.chatappre.classes

data class ChatMessage(
    val date: String? = null,
    val msg: String? = null,
    val receiverMail: String? = null,
    val senderMail: String? = null,
    val time: String? = null,
)
