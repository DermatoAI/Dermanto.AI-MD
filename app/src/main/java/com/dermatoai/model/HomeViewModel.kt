package com.dermatoai.model

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermatoai.helper.Resource
import com.dermatoai.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _recordList = MutableLiveData<List<AnalyzeHistoryData>>()
    val recordList: LiveData<List<AnalyzeHistoryData>> = _recordList

    fun putCurrentLocation(records: List<AnalyzeHistoryData>) {
        _recordList.value = records
    }

    private val _currentLocation = MutableLiveData<Location?>()
    val currentLocation: LiveData<Location?> = _currentLocation

    fun putCurrentLocation(records: Location?) {
        _currentLocation.value = records
    }

    private val _climateInfo = MutableLiveData<Resource<ClimateInfoData>>()
    val climateInfo: LiveData<Resource<ClimateInfoData>> = _climateInfo

    private var lastLat = 0.0
    private var lastLon = 0.0

    fun requestClimateInfo(lat: Double, lon: Double) {
        if (lat == lastLat && lon == lastLon) {
            return
        }

        lastLat = lat
        lastLon = lon

        viewModelScope.launch {
            try {
                weatherRepository.getWeatherInfo(lat, lon).collect { response ->
                    _climateInfo.postValue(response)
                }
            } catch (e: Exception) {
                Log.e("ClimateError", "Error fetching weather data", e)
            }
        }
    }

}