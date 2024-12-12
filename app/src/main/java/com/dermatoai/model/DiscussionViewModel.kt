package com.dermatoai.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dermatoai.api.Diskusi
import com.dermatoai.api.ListDiskusiResponse
import com.dermatoai.repository.DiscussionRepository
import com.dermatoai.repository.LikesRepository
import com.dermatoai.room.LikesEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscussionViewModel @Inject constructor(
    private val repository: DiscussionRepository,
    private val likesRepository: LikesRepository
) : ViewModel() {

    private val _discussions = MutableStateFlow<ListDiskusiResponse?>(null)
    val discussions: StateFlow<ListDiskusiResponse?> = _discussions

    private val _loading: MutableStateFlow<Boolean?> = MutableStateFlow<Boolean?>(false)
    val loading: StateFlow<Boolean?> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchDiscussions(userId: String) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.getListDiscussion()
            val likes = likesRepository.getLikedPosts(userId).groupBy {
                it.discussionId
            }
            if (result.isSuccess) {
                val data = result.getOrNull()
                _discussions.value = data?.copy(
                    data = data.data.map {
                        it.copy(
                            isFavorite = likes[it.id] != null
                        )
                    }.reversed()
                )
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
            _loading.value = false
        }
    }

    fun fetchDiscussionsByUserId(userId: String) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.getListDiscussionUser()
            val likes = likesRepository.getLikedPosts(userId).groupBy {
                it.discussionId
            }
            if (result.isSuccess) {
                val data = result.getOrNull()
                _discussions.value = ListDiskusiResponse(
                    data = (data?.filter { item ->
                        item.authorId == userId
                    }?.map {
                        it.copy(
                            isFavorite = likes[it.id] != null
                        )
                    } ?: emptyList()).asReversed(),
                    status = ""
                )
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
            _loading.value = false
        }
    }

    fun deleteDiscussion(id: String, userId: String, isFromDiscussion: Boolean) {
        viewModelScope.launch {
            _loading.value = true
            val result = repository.deleteDiscussion(id)
            if (result.isSuccess) {
                if (isFromDiscussion) {
                    fetchDiscussions(userId)
                } else {
                    fetchDiscussionsByUserId(userId)
                }
            } else {
                _error.value = result.exceptionOrNull()?.message
            }
            _loading.value = false
        }
    }

    fun like(index: Int, diskusi: Diskusi, userId: String) {
        viewModelScope.launch {
            _loading.value = true
            if (diskusi.isFavorite) {
                likesRepository.removeLike(
                    LikesEntity(
                        discussionId = diskusi.id,
                        judul = diskusi.judul,
                        isi = diskusi.isi,
                        kategori = diskusi.kategori,
                        username = diskusi.authorId,
                        timestamp = diskusi.timestamp,
                        jumlahKomentar = diskusi.jumlahKomentar,
                        userId = userId,
                    )
                )
            } else {
                likesRepository.addLike(diskusi, userId)
            }
            val discussion = _discussions.value
            val discussionList = (discussion?.data ?: emptyList()).toMutableList()
            discussionList[index] = discussionList[index].copy(
                isFavorite = !discussionList[index].isFavorite
            )
            _discussions.value = _discussions.value?.copy(
                data = discussionList
            )
            _loading.value = false
        }
    }
}
