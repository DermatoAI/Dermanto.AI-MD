package com.dermatoai.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermatoai.api.TambahDiskusiResponse
import com.dermatoai.repository.DiscussionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AddDiscussionViewModel @Inject constructor(private val discussionRepository: DiscussionRepository) :
    ViewModel() {

    private val _addDiscussionResult = MutableLiveData<Result<TambahDiskusiResponse>>()
    val addDiscussionResult: LiveData<Result<TambahDiskusiResponse>> = _addDiscussionResult

    fun addDiscussion(
        judul: RequestBody,
        isi: RequestBody,
        kategori: RequestBody,
        idPengguna: RequestBody,
        file: MultipartBody.Part?
    ) {
        viewModelScope.launch {
            try {
                val response =
                    discussionRepository.addDiscussion(judul, isi, kategori, idPengguna, file)
                _addDiscussionResult.postValue(response)
            } catch (e: Exception) {
                _addDiscussionResult.postValue(Result.failure(e))
            }
        }
    }
}
