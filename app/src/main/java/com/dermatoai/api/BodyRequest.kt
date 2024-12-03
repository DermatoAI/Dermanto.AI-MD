package com.dermatoai.api

import com.google.gson.annotations.SerializedName

data class AppointmentRequest(
    @SerializedName("user_id") val userId: String,
    @SerializedName("doctor_id") val doctorId: String,
    @SerializedName("appointment_date") val appointmentDate: String,
    @SerializedName("status") val status: String
)