package com.dermatoai.api

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Response<T>(
    val userId: String,
    val result: T
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

data class ChatBRQS(
    val text: String,
)

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