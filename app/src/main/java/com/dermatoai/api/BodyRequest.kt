package com.dermatoai.api

import com.google.gson.annotations.SerializedName
import java.util.Date

data class AppointmentRequest(
    @SerializedName("user_id") val userId: String,
    @SerializedName("doctor_id") val doctorId: String,
    @SerializedName("appointment_date") val appointmentDate: Date,
    @SerializedName("status") val status: String
)

data class ChatRequest(
    @SerializedName("message") val message: String
)