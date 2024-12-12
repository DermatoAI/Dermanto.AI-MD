package com.dermatoai.api

import androidx.room.Ignore
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ImageAnalysisResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: Result,
) {

    data class Result(
        @SerializedName("userId")
        val userId: String,
        @SerializedName("diagnosis")
        val diagnosis: String,
        @SerializedName("confidence")
        val confidence: Double,
        @SerializedName("imageId")
        val imageId: String,
        @SerializedName("timestamp")
        val timestamp: Date
    )
}

data class ChatResponse(
    @SerializedName("response")
    val message: String?,
    @SerializedName("error")
    val error: String?
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

data class AppointmentResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: AppointmentData
)

data class AppointmentData(
    @SerializedName("id") val id: String?,
    @SerializedName("user_id") val userId: String,
    @SerializedName("doctor_id") val doctorId: String,
    @SerializedName("appointment_date") val appointmentDate: Date,
    @SerializedName("status") val status: String,
)

data class DoctorsResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<Doctor>
)

data class Doctor(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("address")
    val address: String
)

data class SkinAnalysisResponse(
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: Result?,
    @SerializedName("imageId") val imageId: String?
)

data class Result(
    @SerializedName("diagnosis") val diagnosis: String,
    @SerializedName("confidence") val confidence: String,
    @SerializedName("treatmentSuggestions") val treatmentSuggestions: String,
    @SerializedName("timestamp") val timestamp: String
)

data class ErrorResponse(
    @SerializedName("error") val error: String
)

data class TambahDiskusiResponse(
    @SerializedName("id") val idDiskusi: String,
    @SerializedName("createdAt") val timestamp: String
)

data class TambahKomentarRequest(
    @SerializedName("id_diskusi") val idDiskusi: String,
    @SerializedName("isi") val isi: String,
    @SerializedName("id_pengguna") val idPengguna: String
)

data class TambahKomentarResponse(
    @SerializedName("status") val status: String,
    @SerializedName("id_komentar") val idKomentar: Int
)

data class GeneralResponse(
    @SerializedName("status") val status: String
)

data class ListDiskusiResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: List<Diskusi>
)

data class Diskusi(
    @SerializedName("id") val id: String,
    @SerializedName("title") val judul: String,
    @SerializedName("content") val isi: String,
    @SerializedName("category") val kategori: String,
    @SerializedName("authorId") val authorId: String,
    @SerializedName("createdAt") val createdAt: TimeStamp?,
    @SerializedName("jumlah_komentar") val jumlahKomentar: Int,
    @SerializedName("image") val images: String,
    @Ignore val isFavorite: Boolean = false
) {
    val timestamp: String = convertTimestamp(createdAt?.seconds ?: 0L)
}

fun convertTimestamp(timestamp: Long): String {
    val date = Date(timestamp * 1000)
    val format = SimpleDateFormat("HH:mm, dd MMMM yyyy", Locale("id", "ID"))
    return format.format(date)
}

data class TimeStamp(
    @SerializedName("_seconds")
    val seconds: Long
)
