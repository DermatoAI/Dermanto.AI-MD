package com.dermatoai.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dermatoai.api.ChatRequest
import com.dermatoai.helper.ChatData
import com.dermatoai.repository.ChatbotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ChatAiViewModel @Inject constructor(
    private val repository: ChatbotRepository
) : ViewModel() {
    private val _chatMessages = MutableLiveData<MutableList<ChatData>>(mutableListOf())
    val chatMessages: LiveData<MutableList<ChatData>> get() = _chatMessages

    fun addMessage(message: ChatData) {
        val currentList = _chatMessages.value ?: mutableListOf()
        currentList.add(message)
        _chatMessages.value = currentList
    }

    fun clearMessages() {
        _chatMessages.value = mutableListOf()
    }

    suspend fun requestChatbot(message: String) {
        repository.requestChatbot(ChatRequest(message)).also {
            addMessage(ChatData(message, false, Date()))
        }
    }
}