package com.dermatoai.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.dermatoai.api.Doctors
import com.dermatoai.oauth.OauthPreferences
import com.dermatoai.repository.AppointmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class AppointmentViewModel @Inject constructor(
    oauthPreferences: OauthPreferences,
    private val appointmentRepository: AppointmentRepository,
) : ViewModel() {

    private val userId = oauthPreferences.getUserId()

    @OptIn(ExperimentalCoroutinesApi::class)
    val historyAppointment: LiveData<List<AppointmentData>> = userId.flatMapLatest { userId ->
        if (userId != null) {
            appointmentRepository.getAllAppointments(userId).map { record ->
                record.map {
                    AppointmentData(it.time, it.doctorName, "record.location")
                }
            }
        } else {
            flowOf(emptyList())
        }
    }.asLiveData()

    @OptIn(ExperimentalCoroutinesApi::class)
    val getUpcoming: LiveData<AppointmentData?> = userId.flatMapLatest { userId ->
        if (userId != null) {
            appointmentRepository.getUpcomingAppointment(userId).map { record ->
                record?.let {
                    AppointmentData(it.time, it.doctorName, "it.location")
                }
            }
        } else {
            flowOf(null)
        }
    }.asLiveData()

    suspend fun removeAppointment(id: String) {
        userId.collect { userId ->
            userId?.let { appointmentRepository.deleteUpcomingAppointment(it, id) }
        }
    }

    val getAllDoctors: LiveData<List<Doctors>> = liveData(Dispatchers.IO) {
        try {
            val doctors = appointmentRepository.getAllDoctors().data
            emit(doctors)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
}