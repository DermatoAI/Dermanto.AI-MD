package com.dermatoai.model

import androidx.recyclerview.widget.DiffUtil
import java.util.Date

/**
 * class used for data in recycleView.
 * @param id
 * @param date
 * @param issue
 * @param score
 *
 * @property id
 * @property date
 * @property issue
 * @property score
 */
data class AnalyzeHistoryData(
    val id: String,
    val date: Date,
    val issue: String,
    val score: Int
) {
    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<AnalyzeHistoryData>() {
            override fun areItemsTheSame(
                oldItem: AnalyzeHistoryData,
                newItem: AnalyzeHistoryData
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: AnalyzeHistoryData,
                newItem: AnalyzeHistoryData
            ): Boolean =
                oldItem == newItem
        }
    }
}
