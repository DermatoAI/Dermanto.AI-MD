package com.dermatoai.repository

import com.dermatoai.api.AppointmentRequest
import com.dermatoai.api.DermatoBackendEndpoint
import com.dermatoai.api.Doctor
import com.dermatoai.room.AppointmentRecord
import com.dermatoai.room.AppointmentRecordDAO
import java.util.Date
import java.util.UUID
import javax.inject.Inject

class AppointmentRepository @Inject constructor(
    private val dao: AppointmentRecordDAO,
    private val fetch: DermatoBackendEndpoint
) {
    fun getAllAppointments(userId: String) = dao.getAllFinished(userId, Date())

    fun getUpcomingAppointment(userId: String) = dao.getUpcoming(userId, Date())

    suspend fun deleteUpcomingAppointment(userId: String, id: String) {
        fetch.deleteAppointment(id)
        dao.delete(userId, id)
    }


    suspend fun getAllDoctors() = fetch.getAllDoctors()

    suspend fun createAppointment(
        userId: String,
        doctor: Doctor,
        appointmentDate: Date
    ) {
        val appointmentRequest =
            AppointmentRequest(userId, doctor.id, appointmentDate, "pending")
        fetch.createAppointment(appointmentRequest).let {
            dao.add(
                it.data
                    .run {
                        AppointmentRecord(
                            userId = userId,
                            doctorId = doctorId,
                            time = appointmentDate,
                            doctorName = doctor.name,
                            id = id ?: "${UUID.randomUUID()}",
                            location = doctor.address
                        )
                    }
            )
        }
    }
}