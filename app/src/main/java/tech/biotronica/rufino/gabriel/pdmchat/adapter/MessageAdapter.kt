import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tech.biotronica.rufino.gabriel.pdmchat.R
import tech.biotronica.rufino.gabriel.pdmchat.model.Message
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MessageAdapter(private var messages: List<Message>) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.nameTv)
        val messageTextView: TextView = view.findViewById(R.id.messageTv)
        val dateTextView: TextView = view.findViewById(R.id.dateTv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tile_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.nameTextView.text = message.senderName
        holder.messageTextView.text = message.content
        holder.dateTextView.text = formatTimestamp(message.timestamp)
    }

    override fun getItemCount(): Int = messages.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newMessages: List<Message>) {
        messages = newMessages
        notifyDataSetChanged()
    }

    private fun formatTimestamp(timestamp: String): String {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            val date = Date(timestamp.toLong())
            sdf.format(date)
        } catch (e: Exception) {
            "Invalid date"
        }
    }
}
