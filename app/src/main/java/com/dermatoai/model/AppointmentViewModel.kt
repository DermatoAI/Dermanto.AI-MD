package com.dermatoai.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AppointmentViewModel : ViewModel() {
    private val _history = MutableLiveData<List<AppointmentData>>()
    val historyAppointment: LiveData<List<AppointmentData>> = _history

    fun putHistoryAppointment(history: List<AppointmentData>) {
        _history.value = history
    }
}