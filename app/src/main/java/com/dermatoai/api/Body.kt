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

data class AnalyzeImage(
    val confidence: Int,
    val diagnosis: String,
    val timestamp: Date,
    val image: String,
    val treatmentSuggestions: String,
)

data class AppointmentBRS(
    val date: Date,
    val doctor: String,
)

data class ChatBRQS(
    val text: String,
)

data class Response<T>(
    val userId: String,
    val result: T
)