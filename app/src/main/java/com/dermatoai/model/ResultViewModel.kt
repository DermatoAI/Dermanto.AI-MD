package com.dermatoai.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ResultViewModel : ViewModel() {
    private val _successResult = MutableLiveData<AnalyzeImageInfo>()
    val successResult: LiveData<AnalyzeImageInfo> = _successResult

    fun putResult(result: AnalyzeImageInfo) {
        _successResult.value = result
    }
}