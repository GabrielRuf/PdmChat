package tech.biotronica.rufino.gabriel.pdmchat.ui.form

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import tech.biotronica.rufino.gabriel.pdmchat.controller.MessageCreateRtController
import tech.biotronica.rufino.gabriel.pdmchat.databinding.FragmentFormBinding
import tech.biotronica.rufino.gabriel.pdmchat.model.Message

class FormFragment : Fragment() {
    private val messageController: MessageCreateRtController by lazy {
        MessageCreateRtController()
    }
    private var _binding: FragmentFormBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.sendButton.setOnClickListener {
            sendMessage()
        }
        database = Firebase.database.reference
        return root
    }

    private fun sendMessage() {
        val addressee = binding.recipientId.text.toString()
        val messageContent = binding.messageContent.text.toString()
        val sharedPreferences = activity?.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val currentUserName = sharedPreferences?.getString("username", "")

        if (addressee.isNotEmpty() && messageContent.isNotEmpty()) {
            val message = currentUserName?.let {
                Message(
                    addressee = addressee,
                    senderName = it,
                    timestamp = System.currentTimeMillis().toString(),
                    content = messageContent
                )
            }

            if (message != null) {
                messageController.insertMessage(message)
                Toast.makeText(context, "Message sent", Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(context, "Addresse and message must not be empty", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
