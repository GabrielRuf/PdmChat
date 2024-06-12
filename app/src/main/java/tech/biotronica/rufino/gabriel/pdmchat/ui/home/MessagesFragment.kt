package tech.biotronica.rufino.gabriel.pdmchat.ui.home

import MessageAdapter
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import tech.biotronica.rufino.gabriel.pdmchat.databinding.FragmentMessagesBinding
import tech.biotronica.rufino.gabriel.pdmchat.model.Message

class MessagesFragment : Fragment() {

    private lateinit var binding: FragmentMessagesBinding
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMessagesBinding.inflate(inflater, container, false)
        setupRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        databaseReference = FirebaseDatabase.getInstance().getReference("messages")
        fetchMessages()
    }

    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter(listOf())
        binding.messagesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.messagesRecyclerView.adapter = messageAdapter
    }


    private fun fetchMessages() {
        val sharedPreferences = activity?.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val username = sharedPreferences?.getString("username", "")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = mutableListOf<Message>()
                snapshot.children.forEach { dataSnapshot ->
                    try {
                        dataSnapshot.getValue(Message::class.java)?.let { message ->
                            if (message.addressee == username) {
                                messages.add(message)
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("MessagesFragment", "Error parsing message", e)
                    }
                }
                activity?.runOnUiThread {
                    messageAdapter.updateData(messages)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MessagesFragment", "Failed to read value.", error.toException())
            }
        })
    }






}
