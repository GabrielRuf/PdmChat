package tech.biotronica.rufino.gabriel.pdmchat.model

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MessageDaoRtImpl: MessageDao {
    companion object {
        private const val MESSAGE_LIST_ROOT_NODE = "messages"
    }

    private val messageRtDbFbReference = Firebase.database.getReference(
        MESSAGE_LIST_ROOT_NODE
    )

    private val messageList = mutableListOf<Message>()
    private var isFirstValueEvent = true
    init {
        messageRtDbFbReference.addChildEventListener(
            object: ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val message = snapshot.getValue<Message>()
                    if (message != null) {
                        messageList.add(message)
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val message = snapshot.getValue<Message>()
                    if (message != null) {
                        val index = messageList.indexOfFirst { it.senderId == message.senderId }
                        if (index != -1) {
                            messageList[index] = message
                        }
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    val message = snapshot.getValue<Message>()

                    if (message != null) {
                        val index = messageList.indexOfFirst { it.senderId == message.senderId }
                        if (index != -1) {
                            messageList.removeAt(index)
                        }
                    }
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    // NSA
                }

                override fun onCancelled(error: DatabaseError) {
                    // NSA
                }
            }
        )

        messageRtDbFbReference.addListenerForSingleValueEvent(
            object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (isFirstValueEvent) {
                        isFirstValueEvent = false
                        val chats = snapshot.children.mapNotNull { it.getValue(Message::class.java) }
                        if (chats.isNotEmpty()) {
                            messageList.addAll(chats.filterNot { messageList.contains(it) })
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // NSA
                }
            }
        )
    }

    override fun createMessage(message: Message): Int {
        val messageRf = messageRtDbFbReference.push()
        val id = messageRf.key ?: return -1
        message.senderId = id
        messageRf.setValue(message)
        return 1
    }

    override fun retrieveMessages(): MutableList<Message> {
        return messageList
    }

    private fun createOrUpdateMessage(message: Message) {
        messageRtDbFbReference.child(message.senderId).setValue(message)
    }
}