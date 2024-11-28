package com.dermatoai.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    private val _recordList = MutableLiveData<List<AnalyzeHistoryData>>()
    val recordList: LiveData<List<AnalyzeHistoryData>> = _recordList

    fun putRecordList(records: List<AnalyzeHistoryData>) {
        _recordList.value = records
    }

    private val _climateInfo = MutableLiveData<ClimateInfoData>()
    val climateInfo: LiveData<ClimateInfoData> = _climateInfo

    fun putClimateInfo(climateInfo: ClimateInfoData) {
        _climateInfo.value = climateInfo
    }
}