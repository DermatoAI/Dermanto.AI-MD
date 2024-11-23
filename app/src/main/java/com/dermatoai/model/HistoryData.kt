package com.dermatoai.model

import androidx.recyclerview.widget.DiffUtil
import java.util.Date

data class HistoryData(
    val id: String,
    val date: Date,
    val issue: String,
    val score: Int
) {
    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<HistoryData>() {
            override fun areItemsTheSame(oldItem: HistoryData, newItem: HistoryData): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: HistoryData, newItem: HistoryData): Boolean =
                oldItem == newItem
        }
    }
}
