package com.dermatoai.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    private val _recordList = MutableLiveData<List<HistoryData>>()
    val recordList: LiveData<List<HistoryData>> = _recordList

    fun putRecordList(records: List<HistoryData>) {
        _recordList.value = records
    }

    private val _climateInfo = MutableLiveData<ClimateInfoData>()
    val climateInfo: LiveData<ClimateInfoData> = _climateInfo

    fun putClimateInfo(climateInfo: ClimateInfoData) {
        _climateInfo.value = climateInfo
    }
}