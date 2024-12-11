package com.dermatoai.helper

import androidx.recyclerview.widget.DiffUtil
import java.util.Date

data class ChatData(
    val message: String,
    val isSender: Boolean,
    val time: Date
) {
    companion object {
        const val SENT = 0
        const val RECEIVED = 1
        val DIFF_UTIL = object : DiffUtil.ItemCallback<ChatData>() {
            override fun areItemsTheSame(oldItem: ChatData, newItem: ChatData): Boolean {
                return oldItem.time == newItem.time
            }

            override fun areContentsTheSame(oldItem: ChatData, newItem: ChatData): Boolean {
                return oldItem == newItem
            }

        }
    }
}
