package com.dermatoai.model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CaptureViewModel : ViewModel() {
    private val _imageUri = MutableLiveData<Uri?>()
    val imageCaptureUri: LiveData<Uri?> = _imageUri

    fun setImageUri(uri: Uri?) {
        _imageUri.value = uri
    }

    private val _lens = MutableLiveData<Int>()
    val lensState: LiveData<Int> = _lens

    fun setLensState(state: Int) {
        _lens.value = state
    }

    private val _flash = MutableLiveData<Int>()
    val flashState: LiveData<Int> = _flash

    fun changeFlashState(state: Int) {
        _flash.value = state
    }
}