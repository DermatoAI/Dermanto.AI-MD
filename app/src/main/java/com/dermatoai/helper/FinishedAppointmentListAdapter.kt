package com.dermatoai.helper

import android.icu.text.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dermatoai.databinding.AppointmentCardBinding
import com.dermatoai.model.AppointmentData
import com.dermatoai.model.AppointmentData.Companion.DIFF_UTIL

class FinishedAppointmentListAdapter :
    ListAdapter<AppointmentData, FinishedAppointmentListAdapter.ViewModel>(DIFF_UTIL) {

    class ViewModel(val binding: AppointmentCardBinding) : RecyclerView.ViewHolder(binding.root) {
        private val dateFormat: DateFormat = DateFormat.getInstance()
        fun bind(item: AppointmentData?) {
            item?.let {
                binding.appointmentDateText.text = dateFormat.format(it.date)
                binding.doctorNameText.text = it.doctor
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewModel {
        val binding =
            AppointmentCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)
    }

    override fun onBindViewHolder(holder: ViewModel, position: Int) {
        holder.bind(getItem(position))
    }
}