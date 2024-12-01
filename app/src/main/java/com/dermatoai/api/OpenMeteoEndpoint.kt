package com.dermatoai.api

import retrofit2.http.GET
import retrofit2.http.Query

interface OpenMeteoEndpoint {
    @GET("v1/forecast?current=temperature_2m,relative_humidity_2m,is_day,weather_code,cloud_cover,wind_speed_10m&daily=weather_code,uv_index_max,uv_index_clear_sky_max&timezone=auto")
    suspend fun getCurrentWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
    ): Weather
}