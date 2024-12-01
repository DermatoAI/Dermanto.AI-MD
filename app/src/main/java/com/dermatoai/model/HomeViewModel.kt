package com.dermatoai.model

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

    fun putRecordList(records: List<AnalyzeHistoryData>) {
        _recordList.value = records
    }

    private val _climateInfo = MutableLiveData<Resource<ClimateInfoData>>()
    val climateInfo: LiveData<Resource<ClimateInfoData>> = _climateInfo

    fun requestClimateInfo(lat: Double, lon: Double) {
        viewModelScope.launch {
            weatherRepository.getWeatherInfo(lat, lon).collect { response ->
                _climateInfo.postValue(response)
            }
        }
    }

}