package com.dermatoai.model

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dermatoai.api.Doctor
import com.dermatoai.oauth.OauthPreferences
import com.dermatoai.repository.AppointmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(
    oauthPreferences: OauthPreferences,
    private val appointmentRepository: AppointmentRepository,
    @ApplicationContext val context: Context
) : ViewModel() {

    private val userId = oauthPreferences.getUserId()

    @OptIn(ExperimentalCoroutinesApi::class)
    val historyAppointment: LiveData<List<AppointmentData>> = userId.flatMapLatest { userId ->
        if (userId != null) {
            appointmentRepository.getAllAppointments(userId).map { record ->
                record.map {
                    AppointmentData(it.id, it.time, it.doctorName, it.location)
                }
            }
        } else {
            flowOf(emptyList())
        }
    }.asLiveData()

    @OptIn(ExperimentalCoroutinesApi::class)
    val upcoming: LiveData<AppointmentData?> = userId.flatMapLatest { userId ->
        if (userId != null) {
            appointmentRepository.getUpcomingAppointment(userId).map { record ->
                record?.let {
                    AppointmentData(it.id, it.time, it.doctorName, it.location)
                }
            }
        } else {
            flowOf(null)
        }
    }.asLiveData()

    suspend fun removeAppointment(id: String) {
        withContext(Dispatchers.IO) {
            userId.collect { userId ->
                userId?.let { appointmentRepository.deleteUpcomingAppointment(it, id) }
            }
        }
    }

    fun createAppointment(context: Context, doctor: Doctor, dateTime: Date) {
        viewModelScope.launch {
            userId.collect { userId ->
                try {
                    viewModelScope.launch(Dispatchers.IO) {
                        userId?.let {
                            appointmentRepository.createAppointment(
                                userId,
                                doctor,
                                dateTime
                            )
                        }
                    }
                } catch (e: Exception) {
                    Log.d("Appointment", e.message.toString())
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val _allDoctors = MutableLiveData<List<Doctor>>()
    val allDoctors: LiveData<List<Doctor>> = _allDoctors

    fun getAllDoctors() {
        viewModelScope.launch {
            try {
                _allDoctors.postValue(appointmentRepository.getAllDoctors().data)
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}