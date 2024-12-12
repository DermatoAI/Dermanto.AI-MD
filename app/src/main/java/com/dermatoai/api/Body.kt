package com.dermatoai.api

import androidx.room.Ignore
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

data class TambahDiskusiRequest(
    @SerializedName("judul") val judul: String,
    @SerializedName("isi") val isi: String,
    @SerializedName("kategori") val kategori: String,
    @SerializedName("id_pengguna") val idPengguna: String
)

data class TambahDiskusiResponse(
    @SerializedName("status") val status: String,
    @SerializedName("id_diskusi") val idDiskusi: Int,
    @SerializedName("timestamp") val timestamp: String
)

data class TambahKomentarRequest(
    @SerializedName("id_diskusi") val idDiskusi: Int,
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
    @SerializedName("id") val id: Int,
    @SerializedName("judul") val judul: String,
    @SerializedName("isi") val isi: String,
    @SerializedName("kategori") val kategori: String,
    @SerializedName("pengguna") val pengguna: Pengguna,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("jumlah_komentar") val jumlahKomentar: Int,
    @SerializedName("images") val images: List<String>,
    @Ignore val isFavorite: Boolean = false
)

data class Pengguna(
    @SerializedName("id") val id: String,
    @SerializedName("username") val username: String
)