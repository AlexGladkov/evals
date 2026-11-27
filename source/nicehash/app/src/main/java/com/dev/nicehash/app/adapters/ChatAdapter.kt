package com.dev.nicehash.app.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dev.nicehash.R
import com.dev.nicehash.domain.models.ChatMessage

/**
 * Adapter for the global support chat overlay message list
 */
class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private val items = mutableListOf<ChatMessage>()

    fun updateMessages(messages: List<ChatMessage>) {
        items.clear()
        items.addAll(messages)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_chat_message, parent, false)
        return ChatViewHolder(view = view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(item = items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val txtUser: TextView = view.findViewById(R.id.txt_chat_user)
        private val txtBot: TextView = view.findViewById(R.id.txt_chat_bot)

        fun bind(item: ChatMessage) {
            if (item.isUser) {
                txtUser.visibility = View.VISIBLE
                txtBot.visibility = View.GONE
                txtUser.text = item.text
            } else {
                txtUser.visibility = View.GONE
                txtBot.visibility = View.VISIBLE
                txtBot.text = item.text
                txtBot.setBackgroundResource(if (item.isError) R.drawable.chat_bubble_error
                    else R.drawable.chat_bubble_bot)
            }
        }
    }
}
