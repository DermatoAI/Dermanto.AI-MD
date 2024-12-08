package com.dermatoai.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dermatoai.R
import com.dermatoai.databinding.FragmentAppointmentBinding
import com.dermatoai.helper.FinishedAppointmentListAdapter
import com.dermatoai.model.AppointmentViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class AppointmentFragment : Fragment() {
    private lateinit var dialog: Dialog
    private lateinit var binding: FragmentAppointmentBinding
    private val viewModel: AppointmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppointmentBinding.inflate(inflater, container, false)
        dialog = Dialog(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val finishedListAdapter = FinishedAppointmentListAdapter()
        binding.finishedContainerList.apply {
            adapter = finishedListAdapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.toolbar.root.title = "Dermato Connect"

        viewModel.historyAppointment.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                with(binding) {
                    finishedContainerList.visibility = GONE
                    historyEmptyAnima.visibility = VISIBLE
                }
            } else {
                with(binding) {
                    finishedContainerList.visibility = VISIBLE
                    historyEmptyAnima.visibility = GONE
                }
                finishedListAdapter.submitList(it)
            }
        }

        binding.createNewButton.setOnClickListener {
            startCreateAppointment()
            dialog.show()
        }
    }

    @SuppressLint("DefaultLocale")
    private fun startCreateAppointment() {
        dialog.setContentView(R.layout.appoinment_modal)

        val button = dialog.findViewById<MaterialButton>(R.id.create_appointment_button)

        val dataPickerButton = dialog.findViewById<ImageView>(R.id.date_picker_button)
        val dateInput = dialog.findViewById<EditText>(R.id.dateInput)
        dataPickerButton.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date")
                .build()

            datePicker.addOnPositiveButtonClickListener { selection ->
                val date =
                    java.text.SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                        .format(selection)
                dateInput.setText(date)
            }

            datePicker.show(parentFragmentManager, "datePicker")
        }
        val timePickerButton = dialog.findViewById<ImageView>(R.id.time_picker_button)
        val timeInput = dialog.findViewById<EditText>(R.id.timeInput)

        timePickerButton.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Time")
                .build()

            timePicker.addOnPositiveButtonClickListener {
                val time = String.format("%02d:%02d", timePicker.hour, timePicker.minute)
                timeInput.setText(time)
            }

            timePicker.show(parentFragmentManager, "timePicker")
        }

        button.setOnClickListener {
            dialog.dismiss()
        }
    }

}