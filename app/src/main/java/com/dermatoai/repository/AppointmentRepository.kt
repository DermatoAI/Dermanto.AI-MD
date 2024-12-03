package com.dermatoai.repository

import com.dermatoai.api.DermatoEndpoint
import com.dermatoai.room.AppointmentRecordDAO
import java.util.Date
import javax.inject.Inject

class AppointmentRepository @Inject constructor(
    private val dao: AppointmentRecordDAO,
    private val fetch: DermatoEndpoint
) {
    fun getAllAppointments(userId: String) = dao.getAll(userId)

    fun getUpcomingAppointment(userId: String) = dao.getUpcoming(userId, Date())

    fun deleteUpcomingAppointment(userId: String, id: String) = dao.delete(userId, id, Date())
}