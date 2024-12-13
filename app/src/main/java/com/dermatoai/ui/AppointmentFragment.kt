package com.dermatoai.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dermatoai.R
import com.dermatoai.api.Doctor
import com.dermatoai.databinding.FragmentAppointmentBinding
import com.dermatoai.helper.FinishedAppointmentListAdapter
import com.dermatoai.model.AppointmentData
import com.dermatoai.model.AppointmentViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class AppointmentFragment : Fragment() {
    private var currentAppointment: AppointmentData? = null
    private var doctors: List<Doctor>? = null
    private lateinit var arrayAdapter: ArrayAdapter<String?>
    private lateinit var dialog: Dialog
    private lateinit var binding: FragmentAppointmentBinding
    private val viewModel: AppointmentViewModel by viewModels()
    private val finishedListAdapter = FinishedAppointmentListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = Dialog(requireActivity())
        uiBing()
        observeSection()
    }

    private fun observeSection() {
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
        viewModel.getAllDoctors()
        viewModel.allDoctors.observe(viewLifecycleOwner) {
            doctors = it
            arrayAdapter = ArrayAdapter(
                requireContext(),
                R.layout.costume_drop_down_list,
                it.map { doctor -> doctor.name }
            )
        }

        viewModel.upcoming.observe(viewLifecycleOwner) {
            if (it == null) {
                with(binding) {
                    appointmentCard.contextGroup.visibility = INVISIBLE
                    cancelButton.visibility = GONE
                    createNewButton.visibility = VISIBLE
                }
            } else {
                with(binding) {
                    currentAppointment = it
                    appointmentCard.contextGroup.visibility = VISIBLE
                    cancelButton.visibility = VISIBLE
                    createNewButton.visibility = GONE
                    appointmentCard.apply {
                        appointmentDateText.text =
                            SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(it.date)
                        doctorNameText.text = it.doctor
                        doctorLocationText.text = it.location
                        appointmentCard.root.setOnClickListener { _ ->
                            openGoogleMaps(
                                requireContext(),
                                0.0,
                                0.0,
                                it.location
                            )
                        }
                    }
                }
            }
        }
    }

    private fun uiBing() {
        binding.finishedContainerList.apply {
            adapter = finishedListAdapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.toolbar.root.title = "Dermato Connect"

        binding.createNewButton.setOnClickListener {
            startCreateAppointment()
            dialog.show()
        }

        binding.cancelButton.setOnClickListener {
            lifecycleScope.launch {
                currentAppointment
                    ?.id?.let { id -> viewModel.removeAppointment(id) }
            }
        }
    }


    @SuppressLint("DefaultLocale")
    private fun startCreateAppointment() {
        dialog.setContentView(R.layout.appoinment_modal)

        val window = dialog.window
        val layoutParams = window?.attributes
        layoutParams?.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        window?.attributes = layoutParams

        val doctorNameInput = dialog.findViewById<AutoCompleteTextView>(R.id.doctor_name_input)
        if (::arrayAdapter.isInitialized) {
            doctorNameInput.setAdapter(arrayAdapter)
        } else {
            Toast.makeText(requireContext(), "No doctors found", Toast.LENGTH_SHORT).show()
            return
        }

        val button = dialog.findViewById<MaterialButton>(R.id.create_appointment_button)

        val dataPickerButton = dialog.findViewById<ImageView>(R.id.date_picker_button)
        val dateInput = dialog.findViewById<EditText>(R.id.dateInput)
        dataPickerButton.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date")
                .build()

            datePicker.addOnPositiveButtonClickListener { selection ->
                val date =
                    SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
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
            val doctorName = doctorNameInput.text.toString()
            val date = dateInput.text.toString()
            val time = timeInput.text.toString()

            if (doctorName.isEmpty() || date.isEmpty() || time.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            try {
                val dateTimeString = "$date $time"
                val dateTimeFormat = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault())
                val dateTime = dateTimeFormat.parse(dateTimeString)

                Log.d("Appointment", "Doctor: $doctorName, DateTime: $dateTime")
                dateTime?.let { datetime ->
                    doctors?.filter { it.name == doctorName }?.get(0)?.let { it1 ->
                        viewModel.createAppointment(
                            requireContext(),
                            it1,
                            datetime
                        )
                    }
                }

                dialog.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Invalid date or time format", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    companion object {
        fun openGoogleMaps(
            context: Context,
            latitude: Double,
            longitude: Double,
            label: String
        ) {
            val gmmIntentUri = Uri.parse("google.navigation:q=$latitude,$longitude")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")

            if (mapIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(mapIntent)
            } else {
                Toast.makeText(context, "Google Maps is not installed", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}