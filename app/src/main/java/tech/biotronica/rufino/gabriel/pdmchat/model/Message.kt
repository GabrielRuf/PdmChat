package tech.biotronica.rufino.gabriel.pdmchat.model

data class Message(
    var senderId: String = "",
    val senderName: String = "",
    val addressee: String = "",
    val timestamp: String = "",
    val content: String = ""
)
