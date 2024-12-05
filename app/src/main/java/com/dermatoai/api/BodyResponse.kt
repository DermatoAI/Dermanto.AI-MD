package com.dermatoai.api

import com.google.gson.annotations.SerializedName
import java.util.Date

data class ImageAnalysisResponse(
    val message: String,
    val result: Result,
    val userId: String
) {

    data class Result(
        val diagnosis: String,
        val confidence: Float,
        val imageId: String,
        val timestamp: Date
    )
}

data class Weather(
    @SerializedName("current")
    val current: CurrentWeather,
    @SerializedName("daily")
    val daily: DailyWeather,
)

data class CurrentWeather(
    @SerializedName("is_day")
    val isDay: Int,
    @SerializedName("weather_code")
    val weatherCode: Int,
    @SerializedName("cloud_cover")
    val cloudCover: Int,
    @SerializedName("wind_speed_10m")
    val windSpeed: Double,
    @SerializedName("temperature_2m")
    val temperature: Double,
    @SerializedName("relative_humidity_2m")
    val humidity: Int
)

data class DailyWeather(
    @SerializedName("uv_index_max")
    val uviMax: List<Double>,
    @SerializedName("uv_index_clear_sky_max")
    val uviSkyMax: List<Double>
)

data class AppointmentResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: AppointmentData
)

data class AppointmentData(
    @SerializedName("user_id") val userId: String,
    @SerializedName("doctor_id") val doctorId: String,
    @SerializedName("appointment_date") val appointmentDate: Date,
    @SerializedName("status") val status: String
)