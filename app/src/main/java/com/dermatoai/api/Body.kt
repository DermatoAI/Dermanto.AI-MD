package com.dermatoai.api

import java.util.Date


data class LoginBRQ(
    val userId: String
)

data class RegisterBRQ(
    val userId: String,
    val birthDate: Date,
    val nickname: String,
)

data class ClimateInfoBRS(
    val sunType: String,
    val temperature: Int,
    val humidity: Int,
    val force: Int,
    val uvScale: Int,
)

data class AnalyzeImageBRS(
    val confidenceScore: Int,
    val issue: String,
    val time: Long,
    val image: String,
    val additionalInfo: String,
    val userId: String
)

data class AppointmentBRS(
    val date: Date,
    val doctor: String,
)

data class ChatBRQS(
    val text: String,
)
