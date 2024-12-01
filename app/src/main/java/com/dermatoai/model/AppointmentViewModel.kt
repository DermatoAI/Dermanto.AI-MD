package com.dermatoai.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dermatoai.oauth.OauthPreferences
import com.dermatoai.repository.AppointmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
                    AppointmentData(it.time, it.doctorName)
                }
            }
        } else {
            flowOf(emptyList())
        }
    }.asLiveData()
}