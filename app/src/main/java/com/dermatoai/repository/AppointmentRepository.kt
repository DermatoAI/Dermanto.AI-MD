package com.dermatoai.repository

import com.dermatoai.api.DermatoEndpoint
import com.dermatoai.room.AppointmentRecordDAO
import javax.inject.Inject

class AppointmentRepository @Inject constructor(
    private val dao: AppointmentRecordDAO,
    private val fetch: DermatoEndpoint
) {
    fun getAllAppointments(userId: String) = dao.getAll(userId)
}