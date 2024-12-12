package com.dermatoai.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermatoai.api.Diskusi
import com.dermatoai.repository.LikesRepository
import com.dermatoai.room.LikesEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LikesViewModel @Inject constructor(
    private val repository: LikesRepository
) : ViewModel() {

    private val _likes = MutableStateFlow<List<LikesEntity>?>(null)
    val likes: StateFlow<List<LikesEntity>?> = _likes

    private val _loading: MutableStateFlow<Boolean?> = MutableStateFlow<Boolean?>(false)
    val loading: StateFlow<Boolean?> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchLikesByUserId(userId: String) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.getLikedPosts(userId)
            _likes.value = result
            _loading.value = false
        }
    }

    fun addLikes(diskusi: Diskusi, userId: String) {
        viewModelScope.launch {
            _loading.value = true
            repository.addLike(diskusi, userId)
            fetchLikesByUserId(userId)
            _loading.value = false
        }
    }

    fun deleteLikes(likesEntity: LikesEntity, userId: String) {
        viewModelScope.launch {
            _loading.value = true
            repository.removeLike(likesEntity)
            fetchLikesByUserId(userId)
            _loading.value = false
        }
    }
}
