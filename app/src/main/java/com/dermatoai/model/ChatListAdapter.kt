package com.dermatoai.model

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dermatoai.databinding.ItemMessageReceivedBinding
import com.dermatoai.databinding.ItemMessageSentBinding
import com.dermatoai.helper.ChatData
import com.dermatoai.helper.ChatData.Companion.DIFF_UTIL
import com.dermatoai.helper.ChatData.Companion.RECEIVED
import com.dermatoai.helper.ChatData.Companion.SENT
import java.text.SimpleDateFormat
import java.util.Locale

class ChatListAdapter :
    ListAdapter<ChatData, RecyclerView.ViewHolder>(DIFF_UTIL) {

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).isSender) SENT else RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == SENT) {
            val view =
                ItemMessageSentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            SentMessageViewHolder(view)
        } else {
            val view = ItemMessageReceivedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ReceivedMessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)
        if (holder is SentMessageViewHolder) {
            holder.bind(message)
        } else if (holder is ReceivedMessageViewHolder) {
            holder.bind(message)
        }
    }


    class SentMessageViewHolder(itemView: ItemMessageSentBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val textView: TextView = itemView.messageText
        private val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
        private val timeView: TextView = itemView.timeCreate
        fun bind(message: ChatData) {
            timeView.text = formatter.format(message.time)
            textView.text = message.message
        }
    }

    class ReceivedMessageViewHolder(itemView: ItemMessageReceivedBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        private val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
        private val timeView: TextView = itemView.timeCreate
        private val textView: TextView = itemView.messageText
        fun bind(message: ChatData) {
            timeView.text = formatter.format(message.time)
            textView.text = message.message
        }
    }
}