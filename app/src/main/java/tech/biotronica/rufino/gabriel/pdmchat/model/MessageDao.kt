package tech.biotronica.rufino.gabriel.pdmchat.model

interface MessageDao {
    fun createMessage(message: Message): Int
    fun retrieveMessages(): MutableList<Message>
}