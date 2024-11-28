package com.dermatoai.model

import androidx.recyclerview.widget.DiffUtil
import com.dermatoai.model.AppointmentData.Companion.DIFF_UTIL
import java.util.Date

/**
 * used for appointment history in CaptureFragment.
 * @param date
 * @param doctor
 *
 * @property date
 * @property doctor
 * @property DIFF_UTIL used for ListAdapter
 */
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
