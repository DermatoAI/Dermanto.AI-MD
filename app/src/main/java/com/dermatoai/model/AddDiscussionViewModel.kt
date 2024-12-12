package com.dermatoai.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermatoai.api.TambahDiskusiRequest
import com.dermatoai.api.TambahDiskusiResponse
import com.dermatoai.repository.DiscussionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddDiscussionViewModel @Inject constructor(private val discussionRepository: DiscussionRepository) :
    ViewModel() {

    private val _addDiscussionResult = MutableLiveData<Result<TambahDiskusiResponse>>()
    val addDiscussionResult: LiveData<Result<TambahDiskusiResponse>> = _addDiscussionResult

    fun addDiscussion(request: TambahDiskusiRequest) {
        viewModelScope.launch {
            val result = discussionRepository.addDiscussion(request)
            _addDiscussionResult.postValue(result)
        }
    }
}
