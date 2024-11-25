package com.dermatoai.helper

import android.icu.text.DateFormat
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
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
                with(binding) {
                    appointmentMeetButton.visibility = GONE
                    appointmentDateText.text = dateFormat.format(it.date)
                    doctorNameText.text = it.doctor

                    val constraintSet = ConstraintSet()
                    constraintSet.clone(constraintLayout)
                    constraintSet.setHorizontalBias(appointmentDetailButton.id, 1f)
                    constraintSet.applyTo(constraintLayout)
                }
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