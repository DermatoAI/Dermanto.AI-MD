package com.dermatoai.repository

import com.dermatoai.R
import com.dermatoai.api.OpenMeteoEndpoint
import com.dermatoai.api.Weather
import com.dermatoai.helper.Resource
import com.dermatoai.model.ClimateInfoData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val openWeather: OpenMeteoEndpoint
) {
    fun getWeatherInfo(lat: Double, lon: Double): Flow<Resource<ClimateInfoData>> = flow {
        emit(Resource.Loading())
        try {
            val response: Weather = openWeather.getCurrentWeather(lat, lon)
            emit(Resource.Success(response.run {
                ClimateInfoData(
                    cloudCategory = categorizeWeather(current.weatherCode),
                    cloudDescription = getWeatherDescription(current.weatherCode),
                    temperature = current.temperature,
                    humidity = current.humidity,
                    force = current.windSpeed,
                    uvi = daily.uviMax.first(),
                    cloudIcon = getWeatherIcon(current.weatherCode)
                )
            }))
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }.flowOn(Dispatchers.IO)

    private fun getWeatherDescription(code: Int): String {
        return when (code) {
            0 -> "Clear sky"
            1 -> "Mainly clear"
            2 -> "Partly cloudy"
            3 -> "Overcast"
            45, 48 -> "Fog and depositing rime fog"
            51 -> "Light drizzle"
            53 -> "Moderate drizzle"
            55 -> "Dense drizzle"
            56 -> "Light freezing drizzle"
            57 -> "Dense freezing drizzle"
            61 -> "Slight rain"
            63 -> "Moderate rain"
            65 -> "Heavy rain"
            66 -> "Light freezing rain"
            67 -> "Heavy freezing rain"
            71 -> "Slight snowfall"
            73 -> "Moderate snowfall"
            75 -> "Heavy snowfall"
            77 -> "Snow grains"
            80 -> "Slight rain showers"
            81 -> "Moderate rain showers"
            82 -> "Violent rain showers"
            85 -> "Slight snow showers"
            86 -> "Heavy snow showers"
            95 -> "Thunderstorm: Slight or moderate"
            96, 99 -> "Thunderstorm with slight or heavy hail"
            else -> "Unknown"
        }
    }

    private fun categorizeWeather(code: Int): String {
        return when (code) {
            0 -> "Clear Sky"
            in 1..3 -> "Cloudy"
            in 51..57 -> "Precipitation"
            in 61..67 -> "Precipitation"
            in 71..77 -> "Snow"
            in 80..86 -> "Showers"
            95, 96, 99 -> "Thunderstorm"
            else -> "Unknown"
        }
    }

    private fun getWeatherIcon(code: Int): Int {
        return when (code) {
            0 -> R.drawable.weather_clear_sky_10 //10
            1 -> R.drawable.sun_bihind_cloud_7 //7   // Mainly clear (sun behind cloud)
            2 -> R.drawable.sun_behind_cloud_with_ligth_rain_8 //8   // Partly cloudy (sun behind cloud with light rain)
            3 -> R.drawable.weather_cloud_only_1 //1
            45, 48 -> R.drawable.weather_wind_fog_25 //25 Fog and depositing rime fog (wind symbol or fog-like representation)
            51 -> R.drawable.weather_cloud_with_light_rain_2 //2   // Light drizzle (cloud with light rain)
            53 -> R.drawable.weather_cloud_with_moderate_rain_3// 3   // Moderate drizzle (cloud with moderate rain)
            55 -> R.drawable.weather_cloud_with_heavy_rain_4 //4   // Dense drizzle (cloud with heavy rain)
            56 -> R.drawable.weather_cloud_with_light_snow_9 //9   // Light freezing drizzle (cloud with light rain and snowflake)
            57 -> R.drawable.weather_cloud_with_moderate_snow_20 //20  // Dense freezing drizzle (cloud with moderate snow)
            61 -> R.drawable.weather_cloud_with_light_rain_2 //2   // Slight rain (cloud with light rain)
            63 -> R.drawable.weather_cloud_with_moderate_rain_3 //3   // Moderate rain (cloud with moderate rain)
            65 -> R.drawable.weather_cloud_with_heavy_rain_4 //4   // Heavy rain (cloud with heavy rain)
            66 -> R.drawable.weather_cloud_with_light_snow_9 //9   // Light freezing rain (cloud with light rain and snowflake)
            67 -> R.drawable.weather_cloud_with_moderate_snow_20 //20  // Heavy freezing rain (cloud with moderate snow)
            71 -> R.drawable.weather_cloud_with_light_snow_9 //19  // Slight snowfall (cloud with light snow)
            73 -> R.drawable.weather_cloud_with_moderate_snow_20 //20  // Moderate snowfall (cloud with moderate snow)
            75 -> R.drawable.weather_cloud_with_heavy_snow_and_wind_24 //24  // Heavy snowfall (cloud with heavy snow and wind)
            77 -> R.drawable.weather_cloud_with_light_snow_9 //19  // Snow grains (cloud with light snow)
            80 -> R.drawable.weather_cloud_with_light_rain_2 //2   // Slight rain showers (cloud with light rain)
            81 -> R.drawable.weather_cloud_with_moderate_rain_3 //3   // Moderate rain showers (cloud with moderate rain)
            82 -> R.drawable.weather_cloud_with_heavy_rain_4 //4   // Violent rain showers (cloud with heavy rain)
            85 -> R.drawable.weather_cloud_with_light_snow_9 //19  // Slight snow showers (cloud with light snow)
            86 -> R.drawable.weather_cloud_with_moderate_snow_20 //20  // Heavy snow showers (cloud with moderate snow)
            95 -> R.drawable.weather_cloud_with_rain_and_lightning_5 //5   // Thunderstorm: Slight or moderate (cloud with lightning)
            96, 99 -> R.drawable.weather_cloud_with_lightning_and_rain_6 //6   // Thunderstorm with slight or heavy hail (cloud with lightning and rain)
            else -> R.drawable.weather_cloud_for_unknown_30 //30  // Unknown (cloud only)
        }
    }

}