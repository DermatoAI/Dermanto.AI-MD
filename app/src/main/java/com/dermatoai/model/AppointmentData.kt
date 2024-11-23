package com.dermatoai.model

import androidx.recyclerview.widget.DiffUtil
import java.util.Date

data class AppointmentData(
    val date: Date,
    val doctor: String,
) {
    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<AppointmentData>() {
            override fun areItemsTheSame(
                oldItem: AppointmentData,
                newItem: AppointmentData
            ): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: AppointmentData,
                newItem: AppointmentData
            ): Boolean = oldItem == newItem
        }
    }
}
