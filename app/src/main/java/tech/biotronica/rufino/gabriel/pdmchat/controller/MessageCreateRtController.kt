package tech.biotronica.rufino.gabriel.pdmchat.controller

import tech.biotronica.rufino.gabriel.pdmchat.model.Message
import tech.biotronica.rufino.gabriel.pdmchat.model.MessageDao
import tech.biotronica.rufino.gabriel.pdmchat.model.MessageDaoRtImpl
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MessageCreateRtController() {
    private val messageDaoImpl: MessageDao = MessageDaoRtImpl()

    fun insertMessage(message: Message) {
        GlobalScope.launch {
            messageDaoImpl.createMessage(message)
        }
    }
}