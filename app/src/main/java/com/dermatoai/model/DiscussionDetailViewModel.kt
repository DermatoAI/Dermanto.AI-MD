package com.dermatoai.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermatoai.api.Diskusi
import com.dermatoai.api.TambahKomentarRequest
import com.dermatoai.repository.DiscussionRepository
import com.dermatoai.repository.LikesRepository
import com.dermatoai.room.LikesEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscussionDetailViewModel @Inject constructor(
    private val repository: DiscussionRepository,
    private val likesRepository: LikesRepository
) : ViewModel() {

    private val _discussionDetail = MutableStateFlow<Diskusi?>(null)
    val discussionDetail: StateFlow<Diskusi?> = _discussionDetail

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchDiscussionDetail(discussionId: Int) {
        viewModelScope.launch {
            val result = repository.getListDiscussion()
            if (result.isSuccess) {
                val discussion = result.getOrNull()?.data?.find { it.id == discussionId }
                if (discussion != null) {
                    _discussionDetail.value = discussion
                } else {
                    _error.value = "Discussion not found"
                }
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun deleteDiscussion(id: Int) {
        viewModelScope.launch {
            val result = repository.deleteDiscussion(id)
            if (result.isSuccess) {
                _discussionDetail.value = null
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun deleteComment(commentId: Int) {
        viewModelScope.launch {
            val result = repository.deleteComment(commentId)
            if (result.isSuccess) {
                fetchDiscussionDetail(_discussionDetail.value?.id ?: return@launch)
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun addComment(discussionId: Int, content: String, userId: String) {
        viewModelScope.launch {
            val request = TambahKomentarRequest(
                idDiskusi = discussionId,
                isi = content,
                idPengguna = userId
            )
            val result = repository.addComment(request)
            if (result.isSuccess) {
                fetchDiscussionDetail(discussionId)
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
        }
    }

    fun like(diskusi: Diskusi, userId: String) {
        viewModelScope.launch {
            if (diskusi.isFavorite) {
                likesRepository.removeLike(
                    LikesEntity(
                        id = diskusi.id,
                        judul = diskusi.judul,
                        isi = diskusi.isi,
                        kategori = diskusi.kategori,
                        penggunaId = diskusi.pengguna.id,
                        username = diskusi.pengguna.username,
                        timestamp = diskusi.timestamp,
                        jumlahKomentar = diskusi.jumlahKomentar,
                        userId = userId,
                    )
                )
            } else {
                likesRepository.addLike(diskusi, userId)
            }
            _discussionDetail.value?.let {
                _discussionDetail.value = it.copy(
                    isFavorite = !it.isFavorite
                )
            }
        }
    }
}
